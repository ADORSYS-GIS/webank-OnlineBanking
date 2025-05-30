# Build stage
FROM ghcr.io/graalvm/native-image-community:24-muslib AS builder

WORKDIR /build

# Copy the source code
COPY . .

# Build the native executable
RUN ./mvnw -Pnative clean package

# Run stage
FROM gcr.io/distroless/static-debian11:nonroot

WORKDIR /app

# Copy only the native executable from the builder stage
COPY --from=builder /build/online-banking-app/target/online-banking-app /app/online-banking-app

# Expose the port your app runs on
EXPOSE 8081

# Use non-root user
USER nonroot

# Run the native executable
CMD ["/app/online-banking-app"]