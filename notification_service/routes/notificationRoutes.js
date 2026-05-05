const express = require('express');
const router = express.Router();
const notificationController = require('../controllers/notificationController');

/**
 * @openapi
 * /notifications:
 *   post:
 *     summary: Crear y enviar notificación de forma síncrona
 *     operationId: createSyncNotification
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               recipients:
 *                 type: array
 *                 items:
 *                   type: string
 *               channels:
 *                 type: array
 *                 items:
 *                   type: string
 *               message:
 *                 type: object
 *                 properties:
 *                   subject:
 *                     type: string
 *                   body:
 *                     type: string
 *     responses:
 *       '200':
 *         description: Notificación enviada con éxito
 *       '500':
 *         description: Error al enviar la notificación
 */
router.post('/notifications', notificationController.createSyncNotification);

/**
 * @openapi
 * /notifications:
 *   get:
 *     summary: Obtener todas las notificaciones
 *     operationId: getAllNotifications
 *     responses:
 *       '200':
 *         description: Lista de todas las notificaciones
 *       '500':
 *         description: Error al obtener las notificaciones
 */
router.get('/notifications', notificationController.getAllNotifications);

/**
 * @openapi
 * /notifications/{id}:
 *   get:
 *     summary: Obtener una notificación específica
 *     operationId: getNotificationById
 *     parameters:
 *       - name: id
 *         in: path
 *         required: true
 *         description: ID de la notificación a obtener
 *     responses:
 *       '200':
 *         description: Detalles de la notificación solicitada
 *       '404':
 *         description: Notificación no encontrada
 *       '500':
 *         description: Error al obtener la notificación
 */
router.get('/notifications/:id', notificationController.getNotificationById);

// Rutas de salud
/**
 * @openapi
 * /health:
 *   get:
 *     summary: Obtener el estado de salud general del servicio
 *     operationId: getHealth
 *     responses:
 *       '200':
 *         description: Estado general del servicio
 *       '500':
 *         description: Error al obtener el estado de salud
 */
router.get('/health', notificationController.getHealth);

/**
 * @openapi
 * /health/ready:
 *   get:
 *     summary: Comprobar si el servicio está listo para manejar peticiones
 *     operationId: getReadiness
 *     responses:
 *       '200':
 *         description: Información sobre la disponibilidad del servicio
 *       '500':
 *         description: Error al verificar la disponibilidad del servicio
 */
router.get('/health/ready', notificationController.getReadiness);

/**
 * @openapi
 * /health/live:
 *   get:
 *     summary: Comprobar si el servicio está vivo
 *     operationId: getLiveness
 *     responses:
 *       '200':
 *         description: Información sobre el estado de actividad del servicio
 *       '500':
 *         description: Error al verificar la actividad del servicio
 */
router.get('/health/live', notificationController.getLiveness);

module.exports = router;
