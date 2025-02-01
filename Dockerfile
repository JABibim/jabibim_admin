FROM openjdk:17-ea-11-jdk-slim

# 로컬에서 도커 이미지로 복사
COPY ./target/admin-0.0.1-SNAPSHOT.jar /jabibim_admin.jar

# 컨테이너 시작될 때 실행될 명령어
ENTRYPOINT ["java", "-jar", "jabibim_admin.jar"]
