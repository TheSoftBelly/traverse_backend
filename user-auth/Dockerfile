# Build stage
FROM arm32v7/openjdk:17 AS build
WORKDIR /app

# Maven 수동 설치
RUN apt-get update && apt-get install -y maven

COPY pom.xml .
# Download dependencies in a separate layer to improve caching
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM arm32v7/openjdk:17-jre
WORKDIR /app
COPY --from=build /app/target/user-auth-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"] 