FROM openjdk:17-jdk-slim AS builder
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw ./

RUN chmod +x mvnw \
 && apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/*

COPY pom.xml ./
RUN ./mvnw -B -DskipTests dependency:go-offline

COPY src/ ./src

RUN ./mvnw -B -DskipTests clean package

RUN mkdir -p /app/extracted \
    && cp target/*.jar /app/extracted/app.jar \
    && (cd /app/extracted && java -Djarmode=layertools -jar app.jar extract && rm app.jar)


FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /app/extracted/dependencies/            /app/dependencies/
COPY --from=builder /app/extracted/snapshot-dependencies/   /app/snapshot-dependencies/
COPY --from=builder /app/extracted/spring-boot-loader/      /app/spring-boot-loader/
COPY --from=builder /app/extracted/application/             /app/application/

EXPOSE 8081

ENTRYPOINT ["java", "-cp", "/app:/app/dependencies/*:/app/snapshot-dependencies/*:/app/spring-boot-loader/*:/app/application/*", "org.springframework.boot.loader.JarLauncher"]