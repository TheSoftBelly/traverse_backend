# Build stage
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Maven 설치 및 설정
RUN apt-get update && apt-get install -y maven zip unzip && \
    mkdir -p /root/.m2 && \
    echo '<?xml version="1.0" encoding="UTF-8"?>' > /root/.m2/settings.xml && \
    echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"' >> /root/.m2/settings.xml && \
    echo '          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"' >> /root/.m2/settings.xml && \
    echo '          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0' >> /root/.m2/settings.xml && \
    echo '                          https://maven.apache.org/xsd/settings-1.0.0.xsd">' >> /root/.m2/settings.xml && \
    echo '    <mirrors>' >> /root/.m2/settings.xml && \
    echo '        <mirror>' >> /root/.m2/settings.xml && \
    echo '            <id>maven-default-http-blocker</id>' >> /root/.m2/settings.xml && \
    echo '            <mirrorOf>external:http:*</mirrorOf>' >> /root/.m2/settings.xml && \
    echo '            <name>Pseudo repository to mirror external repositories initially using HTTP.</name>' >> /root/.m2/settings.xml && \
    echo '            <url>http://0.0.0.0/</url>' >> /root/.m2/settings.xml && \
    echo '            <blocked>true</blocked>' >> /root/.m2/settings.xml && \
    echo '        </mirror>' >> /root/.m2/settings.xml && \
    echo '    </mirrors>' >> /root/.m2/settings.xml && \
    echo '    <profiles>' >> /root/.m2/settings.xml && \
    echo '        <profile>' >> /root/.m2/settings.xml && \
    echo '            <id>default</id>' >> /root/.m2/settings.xml && \
    echo '            <activation>' >> /root/.m2/settings.xml && \
    echo '                <activeByDefault>true</activeByDefault>' >> /root/.m2/settings.xml && \
    echo '            </activation>' >> /root/.m2/settings.xml && \
    echo '            <properties>' >> /root/.m2/settings.xml && \
    echo '                <maven.test.skip>true</maven.test.skip>' >> /root/.m2/settings.xml && \
    echo '                <maven.javadoc.skip>true</maven.javadoc.skip>' >> /root/.m2/settings.xml && \
    echo '            </properties>' >> /root/.m2/settings.xml && \
    echo '        </profile>' >> /root/.m2/settings.xml && \
    echo '    </profiles>' >> /root/.m2/settings.xml && \
    echo '</settings>' >> /root/.m2/settings.xml

COPY pom.xml .
# Download dependencies in a separate layer to improve caching
RUN mvn dependency:go-offline -Dmaven.wagon.http.readTimeout=300000 -Dmaven.wagon.http.connectionTimeout=300000
COPY src ./src
RUN mvn clean package -DskipTests -Dmaven.wagon.http.readTimeout=300000 -Dmaven.wagon.http.connectionTimeout=300000

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/user-auth-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"] 