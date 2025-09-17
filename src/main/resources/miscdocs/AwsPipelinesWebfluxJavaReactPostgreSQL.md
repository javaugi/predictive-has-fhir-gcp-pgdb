AWS Deployment Pipeline for Spring Boot WebFlux + React App with PostgreSQL
Here's a complete CI/CD pipeline setup to deploy your reactive Spring Boot + React application to AWS using Docker and Kubernetes (EKS).

Prerequisites
    AWS account with appropriate permissions
    EKS cluster configured
    RDS PostgreSQL instance
    Route53 domain (espan360.com) configured
    AWS CLI and eksctl installed and configured
    GitHub repository access

1. Infrastructure as Code (Terraform)
First, let's set up the infrastructure using Terraform.

infra/eks-cluster.tf
terraform
provider "aws" {
  region = "us-east-1"
}

resource "aws_eks_cluster" "espan360" {
  name     = "espan360-cluster"
  role_arn = aws_iam_role.eks_cluster.arn

  vpc_config {
    subnet_ids = [aws_subnet.public_subnet1.id, aws_subnet.public_subnet2.id]
  }

  depends_on = [
    aws_iam_role_policy_attachment.eks_cluster_policy
  ]
}

resource "aws_eks_node_group" "espan360_nodes" {
  cluster_name    = aws_eks_cluster.espan360.name
  node_group_name = "espan360-node-group"
  node_role_arn   = aws_iam_role.eks_nodes.arn
  subnet_ids      = [aws_subnet.public_subnet1.id, aws_subnet.public_subnet2.id]

  scaling_config {
    desired_size = 2
    max_size     = 3
    min_size     = 1
  }

  instance_types = ["t3.medium"]
}
infra/rds.tf
terraform
resource "aws_db_instance" "espan360_postgres" {
  identifier             = "espan360-postgres"
  instance_class         = "db.t3.micro"
  allocated_storage      = 20
  engine                 = "postgres"
  engine_version         = "13.4"
  username               = "espanadmin"
  password               = var.db_password
  db_name                = "espan360db"
  parameter_group_name   = "default.postgres13"
  publicly_accessible    = false
  skip_final_snapshot    = true
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
}
2. Docker Setup
Dockerfile (for Spring Boot backend)
dockerfile
# Build stage
FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x gradlew
RUN ./gradlew bootJar

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=builder /workspace/app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
Dockerfile (for React frontend)
dockerfile
# Build stage
FROM node:18 as builder
WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install

COPY . .
RUN npm run build

# Run stage
FROM nginx:alpine
COPY --from=builder /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
nginx.conf
nginx
server {
    listen 80;
    
    location / {
        root /usr/share/nginx/html;
        index index.html index.htm;
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://backend-service:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
3. Kubernetes Manifests
k8s/backend-deployment.yaml
yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: backend
spec:
  replicas: 2
  selector:
    matchLabels:
      app: backend
  template:
    metadata:
      labels:
        app: backend
    spec:
      containers:
      - name: backend
        image: <ECR_REPO_URL>/backend:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_DATASOURCE_URL
          value: jdbc:postgresql://<RDS_ENDPOINT>:5432/espan360db
        - name: SPRING_DATASOURCE_USERNAME
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: username
        - name: SPRING_DATASOURCE_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: password
---
apiVersion: v1
kind: Service
metadata:
  name: backend-service
spec:
  selector:
    app: backend
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
k8s/frontend-deployment.yaml
yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: frontend
spec:
  replicas: 2
  selector:
    matchLabels:
      app: frontend
  template:
    metadata:
      labels:
        app: frontend
    spec:
      containers:
      - name: frontend
        image: <ECR_REPO_URL>/frontend:latest
        ports:
        - containerPort: 80
---
apiVersion: v1
kind: Service
metadata:
  name: frontend-service
spec:
  type: LoadBalancer
  selector:
    app: frontend
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
k8s/ingress.yaml
yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: espan360-ingress
  annotations:
    kubernetes.io/ingress.class: "alb"
    alb.ingress.kubernetes.io/scheme: "internet-facing"
    alb.ingress.kubernetes.io/target-type: "ip"
    alb.ingress.kubernetes.io/certificate-arn: "<ACM_CERTIFICATE_ARN>"
    alb.ingress.kubernetes.io/listen-ports: '[{"HTTP": 80}, {"HTTPS":443}]'
    alb.ingress.kubernetes.io/actions.ssl-redirect: '{"Type": "redirect", "RedirectConfig": { "Protocol": "HTTPS", "Port": "443", "StatusCode": "HTTP_301"}}'
spec:
  rules:
  - host: "espan360.com"
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: frontend-service
            port:
              number: 80
      - path: /api
        pathType: Prefix
        backend:
          service:
            name: backend-service
            port:
              number: 8080
4. GitHub Actions CI/CD Pipeline
.github/workflows/deploy.yml
yaml
name: Deploy to AWS EKS

on:
  push:
    branches: [ main ]

env:
  AWS_REGION: us-east-1
  ECR_REPOSITORY: espan360
  EKS_CLUSTER: espan360-cluster
  KUBE_CONFIG_FILE: kubeconfig.yaml

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
    - name: Checkout code
      uses: actions/checkout@v3

    - name: Configure AWS credentials
      uses: aws-actions/configure-aws-credentials@v2
      with:
        aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
        aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
        aws-region: ${{ env.AWS_REGION }}

    - name: Login to Amazon ECR
      id: login-ecr
      uses: aws-actions/amazon-ecr-login@v1

    # Build and push backend
    - name: Build backend Docker image
      run: docker build -t $ECR_REPOSITORY/backend:latest -f Dockerfile.backend .
      
    - name: Push backend Docker image
      run: |
        docker tag $ECR_REPOSITORY/backend:latest ${{ steps.login-ecr.outputs.registry }}/$ECR_REPOSITORY/backend:latest
        docker push ${{ steps.login-ecr.outputs.registry }}/$ECR_REPOSITORY/backend:latest

    # Build and push frontend
    - name: Build frontend Docker image
      run: docker build -t $ECR_REPOSITORY/frontend:latest -f Dockerfile.frontend .
      
    - name: Push frontend Docker image
      run: |
        docker tag $ECR_REPOSITORY/frontend:latest ${{ steps.login-ecr.outputs.registry }}/$ECR_REPOSITORY/frontend:latest
        docker push ${{ steps.login-ecr.outputs.registry }}/$ECR_REPOSITORY/frontend:latest

    # Configure kubectl
    - name: Install kubectl
      uses: azure/setup-kubectl@v3

    - name: Configure kubeconfig
      run: |
        aws eks --region $AWS_REGION update-kubeconfig --name $EKS_CLUSTER

    # Deploy to Kubernetes
    - name: Deploy backend
      run: |
        kubectl apply -f k8s/backend-deployment.yaml
        kubectl apply -f k8s/backend-service.yaml

    - name: Deploy frontend
      run: |
        kubectl apply -f k8s/frontend-deployment.yaml
        kubectl apply -f k8s/frontend-service.yaml

    - name: Deploy ingress
      run: kubectl apply -f k8s/ingress.yaml

    - name: Verify deployment
      run: kubectl get pods,svc,ingress
5. Setup Instructions
Initialize Infrastructure:

bash
cd infra
terraform init
terraform apply
Configure GitHub Secrets:

AWS_ACCESS_KEY_ID - AWS access key with EKS and ECR permissions

AWS_SECRET_ACCESS_KEY - Corresponding secret key

DB_PASSWORD - PostgreSQL database password

Create ECR Repositories:

bash
aws ecr create-repository --repository-name espan360/backend
aws ecr create-repository --repository-name espan360/frontend
Create Kubernetes Secrets for DB:

bash
kubectl create secret generic db-secret \
  --from-literal=username=espanadmin \
  --from-literal=password='your_db_password'
Push to GitHub:

bash
git add .
git commit -m "Initial deployment setup"
git push origin main
6. Post-Deployment Steps
Configure DNS:

Get the ALB DNS name from the ingress:

bash
kubectl get ingress espan360-ingress -o jsonpath='{.status.loadBalancer.ingress[0].hostname}'
Create a CNAME record in Route53 for espan360.com pointing to this ALB

Enable HTTPS:

Request an ACM certificate for espan360.com

Update the ingress annotation with the certificate ARN

This pipeline will automatically build, push Docker images to ECR, and deploy to EKS whenever code is pushed to the main branch. 
    The application will be accessible at https://espan360.com.