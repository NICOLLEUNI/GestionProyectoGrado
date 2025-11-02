# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY src ./src

# Descargar dependencias primero (cache eficiente)
RUN mvn dependency:go-offline -B

# Construir la aplicación
RUN mvn clean package -DskipTests -Dcheckstyle.skip=true -Dspotbugs.skip=true

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine

# Instalar herramientas útiles
RUN apk add --no-cache \
    wget \
    curl \
    tzdata

# Configurar zona horaria
ENV TZ=America/Bogota
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -g 1001 -S spring && \
    adduser -S spring -u 1001 -G spring

# Crear directorios necesarios
RUN mkdir -p /app/logs /app/config && \
    chown -R spring:spring /app

# Copiar el JAR construido
COPY --from=builder --chown=spring:spring /app/target/*.jar app.jar

# Copiar configuración adicional si existe
COPY --chown=spring:spring config/ ./config/

USER spring

# Exponer puerto
EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200" \
    SERVER_PORT=8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Entrada con variables de entorno
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]