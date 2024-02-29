# Base image
FROM eclipse-temurin:17-jdk-alpine

# Copy the file to app.jar
COPY build/libs/UniApi-0.0.1-SNAPSHOT.jar app.jar

# Expose the post 8080
EXPOSE 8080

# Run the jar file
CMD ["java","-jar","/app.jar"]