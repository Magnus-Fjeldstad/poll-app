# Dockerizing the PollApp

## Introduction

The goal of this experiment is to containerize a Spring Boot application (PollApp) using Docker. The resulting container
image should be easily distributable, runnable on any machine with Docker installed, and ready for deployment to the
cloud. This document describes the steps taken during the process, including image selection, Dockerfile design, and
improvements such as non-root users and multi-stage builds.

## Prerequisites

Before starting, Docker was verified to be installed and running on the system using:

```
docker system info
```

This command executed successfully, confirming that the Docker daemon is available.

## Choosing a Base Image

To build and package the Spring Boot application, the official `gradle:8.8-jdk21` image was chosen as the build image.
For the runtime stage, a slim JRE-only image based on `eclipse-temurin:21-jre` was used. This keeps the final image
minimal and avoids unnecessary build tools.

## Writing the Dockerfile

A multi-stage Dockerfile was created with the following goals:

* Use Gradle to compile and package the app inside a dedicated build stage
* Copy only the resulting JAR into a lightweight runtime image
* Avoid running the app as root inside the container

The resulting Dockerfile:

```dockerfile
# ==== BUILD STAGE ====
FROM gradle:8.8-jdk21 AS build
WORKDIR /app

COPY --chown=gradle:gradle . .
RUN gradle bootJar --no-daemon

# ==== RUNTIME STAGE ====
FROM eclipse-temurin:21-jre
RUN useradd spring
USER spring

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

## docker-compose Setup

To run the PollApp along with its dependencies (Redis and RabbitMQ), a `docker-compose.yml` configuration was added:

```yaml
version: "3.9"

services:
  pollapp:
    build: .
    container_name: pollapp
    depends_on:
      - redis
      - rabbitmq
    environment:
      SPRING_RABBITMQ_HOST: rabbitmq
      SPRING_RABBITMQ_PORT: 5672
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PORT: 6379
    ports:
      - "8080:8080"

  redis:
    image: redis:7-alpine
    container_name: redis
    ports:
      - "6379:6379"

  rabbitmq:
    image: rabbitmq:3-management
    container_name: rabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"
```

## Improvements

Several best practices were applied:

* Multi-stage build to reduce image size
* Running application as a non-root user
* Using official base images for security and maintainability

## Conclusion

The PollApp has successfully been containerized, making it easier to run locally and deploy across environments. The
multi-stage build approach ensures a small and efficient runtime image, while Docker Compose orchestrates the
application and its dependencies.
