const Microservice = require('../models/Microservice');
const axios = require('axios');

// Función para verificar la salud de un microservicio y enviar notificación si está inactivo
const checkServiceHealth = async (service) => {
  try {
    const response = await axios.get(`${service.endpoint}/health`);
    if (response.status === 200) {
      console.log(`El servicio ${service.name} está activo.`);
    } else {
      await sendNotification(service);
    }
  } catch (error) {
    console.log(`El servicio ${service.name} no está activo.`);
    await sendNotification(service);
  }
};

const sendNotification = async (service) => {
  const now = new Date();
  
  // Si se envió una notificación en los últimos 60 segundos, no enviamos otra
  if (service.lastNotified && (now - service.lastNotified) < 60000) {
    console.log(`Notificación ya enviada para ${service.name} en el último minuto. No se envía otra.`);
    return;
  }

  try {
    const notificationPayload = {
      recipients: service.emails,
      channels: ["Email"],
      message: {
        subject: `Servicio inactivo: ${service.name}`,
        body: `El microservicio ${service.name} en ${service.endpoint} no está activo.`
      }
    };
    await axios.post('http://notification-service:3001/notifications', notificationPayload);
    console.log(`Notificación enviada para el servicio ${service.name}`);

    // Actualiza la última notificación enviada
    service.lastNotified = now;
    await service.save();
  } catch (error) {
    console.error(`Error al enviar notificación para ${service.name}:`, error.message);
  }
};

// Inicia un intervalo de monitoreo para cada microservicio según su frecuencia
const startMonitoringService = (service) => {
  setInterval(() => {
    checkServiceHealth(service);
  }, service.frequency);
};

// Exporta startMonitoringService para usarlo en index.js
exports.startMonitoringService = startMonitoringService;

// Controlador para registrar un nuevo microservicio y comenzar su monitoreo
exports.registerMicroservice = async (req, res) => {
  try {
    const { name, endpoint, frequency, emails } = req.body;
    const newMicroservice = new Microservice({ name, endpoint, frequency, emails });
    await newMicroservice.save();
    
    // Inicia el monitoreo para el microservicio registrado
    startMonitoringService(newMicroservice);

    res.status(201).json({ message: 'Microservicio registrado y monitoreo iniciado' });
  } catch (error) {
    res.status(500).json({ message: 'Error al registrar el microservicio', error });
  }
};

// Controlador para obtener el estado de todos los microservicios registrados
exports.getHealthAll = async (req, res) => {
  try {
    const microservices = await Microservice.find();
    const healthChecks = await Promise.all(
      microservices.map(async (service) => {
        try {
          const healthResponse = await axios.get(`${service.endpoint}/health`);
          return { name: service.name, status: healthResponse.data };
        } catch (error) {
          return { name: service.name, status: 'Offline' };
        }
      })
    );
    res.status(200).json(healthChecks);
  } catch (error) {
    res.status(500).json({ message: 'Error al obtener el estado de los microservicios', error });
  }
};

// Controlador para obtener el estado de un microservicio específico
exports.getHealthSingle = async (req, res) => {
  const { microservicio } = req.params;
  try {
    const service = await Microservice.findOne({ name: microservicio });
    if (!service) {
      return res.status(404).json({ message: 'Microservicio no encontrado' });
    }
    
    // Configura un tiempo límite de espera en la solicitud
    const healthResponse = await axios.get(`${service.endpoint}/health`, { timeout: 5000 }); // 5000 ms = 5 segundos

    // Si la respuesta llega antes de 5 segundos, devuelve el estado
    res.status(200).json({ name: service.name, status: healthResponse.data });
  } catch (error) {
    // Si ocurre un error o el tiempo límite se excede, devuelve 503 para "Servicio no disponible"
    res.status(503).json({ name: microservicio, status: 'Offline' });
  }
};

