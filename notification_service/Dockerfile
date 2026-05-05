# Usa una imagen base de Node.js
FROM node:18

# Define el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos package.json y package-lock.json para instalar las dependencias
COPY notification_service/package*.json ./

# Instala las dependencias del proyecto
RUN npm install

# Copia todo el código del proyecto al contenedor
COPY notification_service/. .

# Exponer el puerto donde correrá la app
EXPOSE 3001

# Comando para ejecutar el servicio
CMD ["node", "app.js"]

