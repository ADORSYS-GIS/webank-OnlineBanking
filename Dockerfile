# Regular JVM build (replacing native GraalVM build)
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build_dir

# Copy project-level pom.xml files for dependency resolution
COPY pom.xml .
COPY online-banking-app/pom.xml online-banking-app/
COPY obs/pom.xml obs/
COPY obs/obs-rest/pom.xml obs/obs-rest/
COPY obs/obs-rest-api/pom.xml obs/obs-rest-api/
COPY obs/obs-service-api/pom.xml obs/obs-service-api/
COPY obs/obs-service-impl/pom.xml obs/obs-service-impl/

# Copy the generated settings.xml
RUN mkdir -p /root/.m2
COPY .docker-m2/settings.xml /root/.m2/settings.xml

# Download dependencies to leverage cache
RUN mvn dependency:go-offline -B

# Copy source files
COPY online-banking-app/src online-banking-app/src
COPY obs/obs-rest/src obs/obs-rest/src
COPY obs/obs-rest-api/src obs/obs-rest-api/src
COPY obs/obs-service-api/src obs/obs-service-api/src
COPY obs/obs-service-impl/src obs/obs-service-impl/src

# Build the application (standard JAR, not native)
RUN mvn clean package -DskipTests

# Runtime stage - use JRE for smaller image
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Create non-root user
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copy the built JAR from builder stage
COPY --from=builder /build_dir/online-banking-app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]