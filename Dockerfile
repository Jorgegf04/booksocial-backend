# syntax=docker/dockerfile:1
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
# El cache de ~/.m2 se reutiliza entre builds aunque cambie el código fuente
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -q
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests -q

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "app.jar"]
