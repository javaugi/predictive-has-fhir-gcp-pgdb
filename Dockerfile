#2. Docker Setup
#Dockerfile (for Spring Boot backend)


# Build stage
FROM eclipse-temurin:21
WORKDIR /workspace/app

#COPY gradlew .
#COPY gradle gradle
#COPY build.gradle .
#COPY settings.gradle .
#COPY src src
#RUN chmod +x gradlew
#RUN ./gradlew  bootJar

COPY mvn* .
COPY src src
RUN chmod +x mvn*
RUN ./mvnw  bootJar

# Run stage
FROM eclipse-temurin:21
WORKDIR /app

COPY --from=builder /workspace/app/build/libs/*.jar app.jar

EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]
