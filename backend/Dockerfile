FROM openjdk:17-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory inside the container
WORKDIR /app

# Copy the backend files from your local `backend` directory to `/app` inside the container
COPY . /app

# Build the backend project with Maven
RUN mvn clean package

# Expose the port the application will use (usually 8080 for Spring Boot apps)
EXPOSE 8080

# Run the application
CMD ["mvn", "spring-boot:run"]
