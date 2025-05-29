# 빌드 단계
FROM eclipse-temurin:17-jdk as build
WORKDIR /app

# Gradle Wrapper 파일 복사
COPY gradlew .
COPY gradle/wrapper/gradle-wrapper.jar gradle/wrapper/
COPY gradle/wrapper/gradle-wrapper.properties gradle/wrapper/

# Gradle 캐시를 위한 레이어 분리
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Gradle 의존성 다운로드 (캐시 레이어)
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon --parallel --build-cache

# 소스 코드 복사 및 빌드
COPY src src
RUN ./gradlew build -x test --no-daemon --parallel --build-cache --max-workers=4

# 런타임 단계
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

ENV JAVA_OPTS="-Xms512m -Xmx512m -Dspring.profiles.active=default -Deureka.client.registerWithEureka=false -Deureka.client.fetchRegistry=false -Deureka.server.waitTimeInMsWhenSyncEmpty=0"

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
