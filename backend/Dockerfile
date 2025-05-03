FROM openjdk:17-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory
WORKDIR /app

# Copy the project files into the container
COPY . .

# Run Maven to build the project
RUN mvn clean package

# Expose the port that the application runs on
EXPOSE 8080

# Start the application
CMD ["mvn", "spring-boot:run"]
