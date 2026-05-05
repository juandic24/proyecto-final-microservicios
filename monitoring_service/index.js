const express = require('express');
const mongoose = require('mongoose');
const monitoringRoutes = require('./routes/monitoringRoutes');
const { startMonitoringService } = require('./controllers/monitoringController');
const Microservice = require('./models/Microservice');
const swaggerJSDoc = require('swagger-jsdoc');
const swaggerUi = require('swagger-ui-express');
const app = express();
app.use(express.json());

// Configuración de Swagger
const swaggerOptions = {
  swaggerDefinition: {
    openapi: '3.0.0',
    info: {
      title: 'Monitoring Service API',
      description: 'API para registrar y monitorear el estado de microservicios.',
      version: '1.0.0',
    },
    servers: [
      {
        url: 'http://monitoring-service:3000',
      },
    ],
  },
  apis: ['./routes/monitoringRoutes.js'], // Rutas donde se encuentran los comentarios de Swagger
};

const swaggerDocs = swaggerJSDoc(swaggerOptions);

// Servir la documentación de la API
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocs));

const mongoURI = process.env.MONGO_URI || 'mongodb://mongo:27017/monitoring';
const connectWithRetry = () => {
  console.log('Intentando conectar a MongoDB...');
  mongoose.connect(mongoURI, { useNewUrlParser: true, useUnifiedTopology: true })
    .then(async () => {
      console.log('Conexión exitosa a MongoDB');
      
      // Recupera y comienza el monitoreo de todos los microservicios registrados
      const microservices = await Microservice.find();
      microservices.forEach((service) => startMonitoringService(service));
    })
    .catch((err) => {
      console.error('Error al conectar a MongoDB, reintentando en 5 segundos...', err);
      setTimeout(connectWithRetry, 5000); // Reintenta después de 5 segundos
    });
};

connectWithRetry();
app.use(monitoringRoutes);

const PORT = 3000;
app.listen(PORT, () => {
  console.log(`Servidor corriendo en el puerto ${PORT}`);
});
