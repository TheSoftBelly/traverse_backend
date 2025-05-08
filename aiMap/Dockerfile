# 1단계: 빌드용 (build stage)
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# 소스 코드 복사
COPY . .

# Gradle 빌드 실행
RUN gradle bootJar --no-daemon

# 2단계: 실행용 (runtime stage)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 빌드된 jar 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 오픈
EXPOSE 8081

# 실행
ENTRYPOINT ["java","-jar","/app/app.jar"]
