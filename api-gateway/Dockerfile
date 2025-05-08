FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# 빌드 캐시 최적화를 위해 Gradle 파일을 먼저 복사
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN chmod +x ./gradlew
RUN ./gradlew wrapper --gradle-version 8.5
RUN ./gradlew dependencies

# 소스 코드 복사 및 빌드
COPY . .
RUN ./gradlew build -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/api-gateway-0.0.1-SNAPSHOT.jar /app/app.jar

# JVM 메모리 설정
ENV JAVA_OPTS="-Xms512m -Xmx512m"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"] 