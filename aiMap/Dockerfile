# 1단계: 빌드용 (build stage)
FROM arm32v7/openjdk:17 AS build
WORKDIR /app

# Gradle 수동 설치
RUN apt-get update && apt-get install -y curl unzip && \
    curl -s https://get.sdkman.io | bash && \
    bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install gradle 8.5"

# 소스 코드 복사
COPY . .

# Gradle 빌드 실행
RUN gradle bootJar --no-daemon

# 2단계: 실행용 (runtime stage)
FROM arm32v7/openjdk:17-jre
WORKDIR /app

# 빌드된 jar 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 오픈
EXPOSE 8081

# 실행
ENTRYPOINT ["java","-jar","/app/app.jar"]
