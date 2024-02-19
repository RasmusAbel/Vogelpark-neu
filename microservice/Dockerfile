# Use the official gradle/Java 21 image to create a build artifact.
FROM gradle:8.6.0-jdk21 as builder

# Set the working directory in the image to /app
WORKDIR /app

# Copy the build.gradle file to our app directory
COPY build.gradle .

# Download all required dependencies into one layer
RUN gradle dependencies

# Copy the rest of the project
COPY src ./src

# Build the project
RUN gradle clean build

# Use OpenJDK 21 for the runtime stage
FROM openjdk:21-jdk

# Set the working directory in the image to /app
WORKDIR /app

# Copy the jar file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Run the application
CMD ["java", "-jar", "app.jar"]
