// services/healthService.js

const amqp = require('amqplib');
const os = require('os');
const process = require('process');
const moment = require('moment');  // Instala moment para manejar tiempos de forma sencilla

const RABBITMQ_HOST = process.env.RABBITMQ_HOST || 'rabbitmq';
const RABBITMQ_PORT = process.env.RABBITMQ_PORT || '5672';
const RABBITMQ_USER = process.env.RABBITMQ_USER || 'admin';
const RABBITMQ_PASS = process.env.RABBITMQ_PASS || 'root';

class HealthService {

  // Verificar si el servicio está vivo (si el servidor responde)
  async checkLiveness() {
    try {
      const uptime = moment.duration(process.uptime(), 'seconds').humanize();  // Formato legible del uptime
      return {
        status: "UP",
        version: "1.0.0",
        uptime: uptime,
      };
    } catch (error) {
      console.error('Error al comprobar el estado del servicio:', error);
      return {
        status: "DOWN",
        version: "1.0.0",
        uptime: "0:00:00.000000",
      };
    }
  }

  // Verificar si el servicio está listo (si RabbitMQ está accesible)
  async checkReadiness() {
    try {
      // Intentar conectar con RabbitMQ
      const connection = await amqp.connect(`amqp://${RABBITMQ_USER}:${RABBITMQ_PASS}@${RABBITMQ_HOST}:${RABBITMQ_PORT}`);  // Asume que RABBITMQ_URL está en .env
      await connection.close();  // Cerrar la conexión después de verificar

      return {
        status: "UP",
        data: {
          from_: moment().toISOString(),
          status: "READY"
        }
      };
    } catch (error) {
      console.error('Error al verificar la conexión con RabbitMQ:', error);
      return {
        status: "DOWN",
        data: {
          from_: moment().toISOString(),
          status: "NOT READY"
        }
      };
    }
  }

  // Obtener el estado general del servicio (vivo y listo)
  async getHealth() {
    const liveness = await this.checkLiveness();
    const readiness = await this.checkReadiness();

    return {
      status: "UP",
      checks: [
        {
          name: "Readiness check",
          status: readiness.status,
          data: readiness.data
        },
        {
          name: "Liveness check",
          status: liveness.status,
          data: {
            from_: moment().toISOString(),
            status: liveness.status === "UP" ? "ALIVE" : "DEAD"
          }
        }
      ]
    };
  }
}

module.exports = new HealthService();
