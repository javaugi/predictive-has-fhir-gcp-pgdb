#infra/rds.tf
#terraform
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
