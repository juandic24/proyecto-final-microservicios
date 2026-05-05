// notificationController.js
const notificationService = require('../services/notificationService');
const healthService = require('../services/healthService');  // Importa el servicio de salud

// Crear una notificación sincrónica
exports.createSyncNotification = async (req, res) => {
  try {
    const notificationData = req.body;
    const notification = await notificationService.sendSyncNotification(notificationData);
    res.status(200).json(notification);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Obtener todas las notificaciones
exports.getAllNotifications = async (req, res) => {
  try {
    const notifications = await notificationService.getAllNotifications();
    res.status(200).json(notifications);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Obtener una notificación por ID
exports.getNotificationById = async (req, res) => {
  try {
    const notification = await notificationService.getNotificationById(req.params.id);
    res.status(200).json(notification);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Ruta /health
exports.getHealth = async (req, res) => {
  try {
    const health = await healthService.getHealth();
    res.json(health);
  } catch (error) {
    console.error('Error al obtener el estado de salud:', error);
    res.status(500).json({ status: 'DOWN' });
  }
};

// Ruta /health/ready
exports.getReadiness = async (req, res) => {
  try {
    const readiness = await healthService.checkReadiness();
    res.json(readiness);
  } catch (error) {
    console.error('Error al comprobar si el servicio está listo:', error);
    res.status(500).json({ status: 'DOWN', message: 'Not ready' });
  }
};

// Ruta /health/live
exports.getLiveness = async (req, res) => {
  try {
    const liveness = await healthService.checkLiveness();
    res.json(liveness);
  } catch (error) {
    console.error('Error al comprobar si el servicio está vivo:', error);
    res.status(500).json({ status: 'DOWN', message: 'Not live' });
  }
};
