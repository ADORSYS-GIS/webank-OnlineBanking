FROM vegardit/graalvm-maven:latest-java17 AS builder

WORKDIR /build_dir

# Set build-time variables for GitHub credentials
ARG GH_USERNAME
ARG GH_PASSWORD

# Copy project-level pom.xml files for dependency resolution
COPY pom.xml .
COPY online-banking-app/pom.xml online-banking-app/
COPY obs/pom.xml obs/
COPY obs/obs-rest/pom.xml obs/obs-rest/
COPY obs/obs-rest-api/pom.xml obs/obs-rest-api/
COPY obs/obs-service-api/pom.xml obs/obs-service-api/
COPY obs/obs-service-impl/pom.xml obs/obs-service-impl/

# Provision Maven settings with GitHub credentials
COPY settings.template.xml /root/.m2/settings.xml
RUN sed -i "s|GH_USERNAME|${GH_USERNAME}|g" /root/.m2/settings.xml && \
    sed -i "s|GH_PASSWORD|${GH_PASSWORD}|g" /root/.m2/settings.xml

# Download dependencies to leverage cache
RUN mvn dependency:go-offline -B

# Copy source files only (to avoid unnecessary rebuilds)
COPY online-banking-app/src online-banking-app/src
COPY obs/obs-rest/src obs/obs-rest/src
COPY obs/obs-rest-api/src obs/obs-rest-api/src
COPY obs/obs-service-api/src obs/obs-service-api/src
COPY obs/obs-service-impl/src obs/obs-service-impl/src


RUN mvn package -Pnative -DskipTests \
    && cp online-banking-app/target/online-banking-app-*-SNAPSHOT /build_dir/server


FROM debian:12-slim AS deps

ARG TARGETPLATFORM
ARG BUILDPLATFORM

RUN apt-get update && apt-get install -y \
    zlib1g \
    libc6 \
    && rm -rf /var/lib/apt/lists/*

RUN case "$TARGETPLATFORM" in \
    "linux/amd64") ARCH_DIR="x86_64-linux-gnu"; LOADER="ld-linux-x86-64.so.2"; LOADER_PATH="/lib64" ;; \
    "linux/arm64") ARCH_DIR="aarch64-linux-gnu"; LOADER="ld-linux-aarch64.so.1"; LOADER_PATH="/lib" ;; \
    *) echo "Unsupported platform: $TARGETPLATFORM" && exit 1 ;; \
    esac && \
    echo "ARCH_DIR=$ARCH_DIR" > /tmp/arch_info && \
    echo "LOADER=$LOADER" >> /tmp/arch_info && \
    echo "LOADER_PATH=$LOADER_PATH" >> /tmp/arch_info

RUN . /tmp/arch_info && \
    mkdir -p /deps/lib/$ARCH_DIR /deps/lib64 /deps/lib && \
    cp /lib/$ARCH_DIR/libz.so.1 /deps/lib/$ARCH_DIR/ || true && \
    cp /lib/$ARCH_DIR/libc.so.6 /deps/lib/$ARCH_DIR/ || true && \
    if [ "$LOADER_PATH" = "/lib64" ]; then cp $LOADER_PATH/$LOADER /deps/lib64/; else cp $LOADER_PATH/$LOADER /deps/lib/; fi && \
    find /lib/$ARCH_DIR -name "*.so*" -exec cp {} /deps/lib/$ARCH_DIR/ \; 2>/dev/null || true


# Final image
FROM gcr.io/distroless/static-debian12:nonroot

WORKDIR /app
EXPOSE 8081

ARG TARGETPLATFORM

COPY --from=deps /deps/lib/ /lib/
COPY --from=deps /deps/lib64/ /lib64/
COPY --from=builder /build_dir/server ./


ENTRYPOINT ["/app/server"]
