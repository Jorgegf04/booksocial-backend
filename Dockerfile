# syntax=docker/dockerfile:1

# ── Stage 1: build ────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copiar solo el manifiesto primero para aprovechar la caché de capas:
# si el código cambia pero pom.xml no, las dependencias no se re-descargan.
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -q

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests -q

# ── Stage 2: runtime ──────────────────────────────────────────────────────────
# Imagen alpine: ~60% más ligera que la variante jammy.
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Usuario sin privilegios para minimizar el impacto de una brecha de seguridad.
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# curl para el healthcheck del contenedor.
RUN apk add --no-cache curl

# Directorio de imágenes con permisos para el usuario de la app.
# Debe coincidir con UPLOAD_DIR=/app/images en docker-compose.yml y el volumen images_data.
RUN mkdir -p /app/images && chown appuser:appgroup /app/images

COPY --from=builder --chown=appuser:appgroup /app/target/*.jar app.jar

USER appuser

EXPOSE 9999

ENTRYPOINT ["java", "-jar", "app.jar"]
