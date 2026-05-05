const express = require('express');
const mongoose = require('mongoose');
const notificationRoutes = require('./routes/notificationRoutes'); 
const rabbitmqListener = require('./listeners/rabbitmqListener');
const swaggerJsdoc = require('swagger-jsdoc');
const swaggerUi = require('swagger-ui-express');
require('dotenv').config();

const app = express();
app.use(express.json());

// Configuración de Swagger
const swaggerOptions = {
  definition: {
    openapi: '3.0.0',
    info: {
      title: 'Notification Service API',
      version: '1.0.0',
      description: 'API para gestionar notificaciones',
    },
  },
  apis: ['./routes/*.js'], 
};

// Generar la especificación de OpenAPI
const swaggerDocs = swaggerJsdoc(swaggerOptions);

// Ruta para mostrar la interfaz interactiva de Swagger
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocs));

mongoose.connect(process.env.MONGO_URI, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
}).then(() => console.log('Connected to MongoDB'))
  .catch(err => console.error('MongoDB connection error:', err));

// Usar las rutas de notificación
app.use(notificationRoutes);

// Escuchar mensajes en la cola de notificaciones
rabbitmqListener;

const PORT = 3001;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
