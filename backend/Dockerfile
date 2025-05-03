FROM openjdk:17-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory
WORKDIR /app

# Copy the backend files
COPY backend/ .

# Build the project with Maven
RUN mvn clean package

# Expose the port that the app will run on
EXPOSE 8080

# Start the application
CMD ["mvn", "spring-boot:run"]
