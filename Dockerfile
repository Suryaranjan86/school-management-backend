# Multi-stage Dockerfile to build the Spring Boot backend and produce a small runtime image
FROM maven:3.9.4-eclipse-temurin-17 as builder
WORKDIR /workspace/app

# Copy Maven metadata first to take advantage of Docker layer caching
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN mvn -B -f pom.xml -DskipTests dependency:go-offline

# Copy sources and build the application
COPY src ./src
RUN mvn -B package -DskipTests

## Runtime image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the packaged jar (adjust name if your artifactId/version differ)
COPY --from=builder /workspace/app/target/school-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]


