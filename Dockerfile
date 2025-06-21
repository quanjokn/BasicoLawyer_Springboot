# Base image
FROM eclipse-temurin:17-jdk-alpine

# Tạo thư mục app
WORKDIR /app

# Copy file JAR đã build từ Maven
COPY target/BasicoLawer-0.0.1-SNAPSHOT.jar app.jar

# Cấu hình cổng
EXPOSE 8080

# Lệnh chạy Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
