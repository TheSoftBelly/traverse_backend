FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# 소스 코드 전체 복사
COPY . .
RUN chmod +x ./gradlew

# Gradle 빌드 실행
RUN ./gradlew build -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/api-gateway-0.0.1-SNAPSHOT.jar /app/app.jar

# JVM 메모리 설정
ENV JAVA_OPTS="-Xms512m -Xmx512m"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"] 