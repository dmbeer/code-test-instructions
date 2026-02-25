# -------- Build stage --------
FROM gradle:9.3-jdk21 AS builder

WORKDIR /app

# Copy Gradle wrapper & config first (cache-friendly)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

# Gradle dependency cache
RUN --mount=type=cache,target=/home/gradle/.gradle \
    ./gradlew dependencies --no-daemon

# Copy source code
COPY src src

# Build with cache
RUN --mount=type=cache,target=/home/gradle/.gradle \
    ./gradlew build -x test --no-daemon

# -------- Runtime stage --------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
