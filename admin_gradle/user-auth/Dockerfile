# Base image: OpenJDK with Alpine
FROM openjdk:17-jdk-alpine

# Define the path to the application JAR file
ARG APP_JAR_PATH=build/libs/*.jar

# Copy the JAR file to the container
COPY ${APP_JAR_PATH} app.jar

# Define the application execution entry point
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Extracted constant for the exposed application port
ARG EXPOSED_PORT=8082

# Expose the application port
EXPOSE ${EXPOSED_PORT}