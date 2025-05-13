FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Gradle 설치 - 캐시 레이어 최적화
RUN apt-get update && apt-get install -y curl unzip zip && \
    curl -O https://downloads.gradle.org/distributions/gradle-8.5-bin.zip && \
    unzip gradle-8.5-bin.zip && \
    mv gradle-8.5 /opt/gradle && \
    ln -s /opt/gradle/bin/gradle /usr/local/bin/gradle && \
    rm gradle-8.5-bin.zip

# 소스 코드 복사
COPY . .

# Gradle Wrapper 생성 및 빌드 - 빌드 성능 최적화
RUN gradle wrapper --gradle-version 8.5
RUN chmod +x ./gradlew
RUN ./gradlew build -x test --no-daemon --parallel --build-cache

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/api-gateway-0.0.1-SNAPSHOT.jar /app/app.jar

# JVM 메모리 설정
ENV JAVA_OPTS="-Xms512m -Xmx512m"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"] 