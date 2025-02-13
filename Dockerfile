FROM openjdk:17-ea-11-jdk-slim

# ffmpeg 및 ffprobe 설치 후 심볼릭 링크 생성
RUN apt-get update && \
    apt-get install -y ffmpeg && \
    ln -s /usr/bin/ffmpeg /usr/local/bin/ffmpeg && \
    ln -s /usr/bin/ffprobe /usr/local/bin/ffprobe && \
    rm -rf /var/lib/apt/lists/*  # 캐시 정리로 이미지 크기 줄이기

# JAR 파일 복사
COPY ./target/admin-0.0.1-SNAPSHOT.jar /jabibim_admin.jar

# 컨테이너 실행 명령어
ENTRYPOINT ["java", "-jar", "jabibim_admin.jar"]
