const express = require('express');
const router = express.Router();
const monitoringController = require('../controllers/monitoringController');

/**
 * @openapi
 * /register:
 *   post:
 *     summary: Registrar un nuevo microservicio y comenzar su monitoreo
 *     operationId: registerMicroservice
 *     requestBody:
 *       description: Datos necesarios para registrar un microservicio
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               name:
 *                 type: string
 *                 example: "User Service"
 *               endpoint:
 *                 type: string
 *                 example: "http://user-service:3000"
 *               frequency:
 *                 type: integer
 *                 example: 10
 *               emails:
 *                 type: array
 *                 items:
 *                   type: string
 *                   example: ["admin@example.com"]
 *     responses:
 *       '201':
 *         description: Microservicio registrado exitosamente
 *       '500':
 *         description: Error al registrar el microservicio
 */
router.post('/register', monitoringController.registerMicroservice);

/**
 * @openapi
 * /health:
 *   get:
 *     summary: Obtener el estado de todos los microservicios registrados
 *     operationId: getHealthAll
 *     responses:
 *       '200':
 *         description: Estado de todos los microservicios registrados
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 type: object
 *                 properties:
 *                   name:
 *                     type: string
 *                     example: "User Service"
 *                   status:
 *                     type: string
 *                     example: "Online"
 *       '500':
 *         description: Error al obtener el estado de los microservicios
 */
router.get('/health', monitoringController.getHealthAll);

/**
 * @openapi
 * /health/{microservicio}:
 *   get:
 *     summary: Obtener el estado de un microservicio específico
 *     operationId: getHealthSingle
 *     parameters:
 *       - name: microservicio
 *         in: path
 *         required: true
 *         schema:
 *           type: string
 *         description: Nombre del microservicio
 *     responses:
 *       '200':
 *         description: Estado del microservicio especificado
 *       '404':
 *         description: Microservicio no encontrado
 *       '503':
 *         description: Microservicio inactivo o no disponible
 */
router.get('/health/:microservicio', monitoringController.getHealthSingle);

module.exports = router;

