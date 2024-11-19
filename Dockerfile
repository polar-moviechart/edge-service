# 1단계: 빌드 단계
FROM eclipse-temurin:17 AS builder
WORKDIR /workspace

# Gradle 관련 파일 복사 및 의존성 캐싱
COPY build.gradle settings.gradle gradlew /workspace/
COPY gradle /workspace/gradle
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사 및 JAR 파일 빌드
COPY src /workspace/src
RUN ./gradlew clean build --no-daemon --parallel

# 빌드된 JAR 파일 추출 (계층화 지원)
ARG JAR_FILE=build/libs/edge-service-0.0.1-SNAPSHOT.jar
RUN java -Djarmode=layertools -jar ${JAR_FILE} extract

# 2단계: 실행 단계
FROM eclipse-temurin:17
RUN apt-get update && \
    apt-get install -y vim && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 'spring' 유저 생성 및 워크스페이스 설정
RUN useradd spring
USER spring
WORKDIR /workspace

# 1단계에서 추출된 파일 복사
COPY --from=builder /workspace/dependencies/ ./
COPY --from=builder /workspace/spring-boot-loader/ ./
COPY --from=builder /workspace/snapshot-dependencies/ ./
COPY --from=builder /workspace/application/ ./

# 애플리케이션 실행
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]