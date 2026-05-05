# Usa una imagen base de Node.js
FROM node:18

# Define el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos package.json y package-lock.json para instalar las dependencias
COPY monitoring_service/package*.json ./

# Instala las dependencias del proyecto
RUN npm install

# Copia todo el código del proyecto al contenedor
COPY monitoring_service/. .

# Exponer el puerto donde correrá la app
EXPOSE 3000

# Comando para ejecutar el servicio
CMD ["node", "index.js"]

