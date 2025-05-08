# 1단계: 빌드용 (build stage)
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# 필요한 파일만 복사하고 의존성 먼저 다운로드 (캐시 최적화)
COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN gradle build --no-daemon || return 0

# 나머지 소스 복사 후 빌드
COPY src src
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
