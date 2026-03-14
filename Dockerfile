FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy pre-built jar file
COPY target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
