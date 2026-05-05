# Usa una imagen base de Python
FROM python:3.12-slim

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /logs_service

# Copiar el archivo requirements.txt e instalar las dependencias
COPY logs_service/requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt && pip check

# Copiar el resto del código de la aplicación al contenedor
COPY logs_service/. .

# Exponer el puerto en el que el microservicio escucha 
EXPOSE 8000

# Comando para ejecutar la aplicación 
CMD ["python", "main.py"]

