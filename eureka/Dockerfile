# 빌드 단계
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .

# Gradle 수동 설치
RUN apt-get update && apt-get install -y curl unzip zip && \
    curl -s https://get.sdkman.io | bash && \
    bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install gradle 8.5"

# Gradle Wrapper 생성 및 빌드
RUN gradle wrapper --gradle-version 8.5
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

# 런타임 단계
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/eureka-0.0.1-SNAPSHOT.jar /app/app.jar

ENV JAVA_OPTS="-Xms512m -Xmx512m -Dspring.profiles.active=default -Deureka.client.registerWithEureka=false -Deureka.client.fetchRegistry=false -Deureka.server.waitTimeInMsWhenSyncEmpty=0"

EXPOSE 8761
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
