#!/usr/bin/env bash

set -e  # Detiene la ejecución si ocurre un error
set -o pipefail  # Falla si algún comando en un pipeline falla

echo "🚀 Iniciando proceso de construcción..."

# Construir el JAR con Gradle
echo "🔨 Compilando la aplicación con Gradle..."
if ./gradlew bootJar; then
    echo "✅ Compilación exitosa."
else
    echo "❌ Error en la compilación." >&2
    exit 1
fi

# Construir la imagen Docker sin usar caché
echo "🐳 Construyendo imagen Docker..."
if docker build -t prueba-tecnica . --no-cache; then
    echo "✅ Imagen Docker construida correctamente."
else
    echo "❌ Error en la construcción de la imagen Docker." >&2
    exit 1
fi
# Reiniciar los contenedores con Docker Compose
echo "♻️ Reiniciando servicios con Docker Compose..."
docker-compose down && docker-compose up

echo "✅ Todo listo. La aplicación está corriendo. 🚀"
