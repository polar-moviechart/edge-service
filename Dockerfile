# 1단계: 빌드 단계
FROM eclipse-temurin:17 AS builder
WORKDIR /workspace

ARG JAR_FILE=build/libs/edge-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} edge-service.jar
# 빌드된 JAR 파일 추출 (계층화 지원)
RUN java -Djarmode=layertools -jar edge-service.jar extract

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