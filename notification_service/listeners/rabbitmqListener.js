const amqp = require('amqplib');
const notificationService = require('../services/notificationService');
require('dotenv').config();

const RABBITMQ_HOST = process.env.RABBITMQ_HOST || 'rabbitmq';
const RABBITMQ_PORT = process.env.RABBITMQ_PORT || '5672';
const RABBITMQ_USER = process.env.RABBITMQ_USER || 'admin';
const RABBITMQ_PASS = process.env.RABBITMQ_PASS || 'root';
const QUEUE_NAME = 'notifications_queue';

let connection, channel;

const connectRabbitMQ = async () => {
  try {
    connection = await amqp.connect(`amqp://${RABBITMQ_USER}:${RABBITMQ_PASS}@${RABBITMQ_HOST}:${RABBITMQ_PORT}`);
    channel = await connection.createChannel();

    await channel.assertQueue(QUEUE_NAME, { durable: true });

    console.log('Conexión exitosa a RabbitMQ');

    // Escuchar en la cola de notificaciones
    channel.consume(QUEUE_NAME, async (msg) => {
      if (msg !== null) {
        try {
          const message = msg.content.toString();
          console.log('Mensaje recibido:', message);
          // Parsear el mensaje recibido como JSON
          const notificationData = JSON.parse(message);

          // Validar que el mensaje tenga la estructura esperada
          if (isValidNotification(notificationData)) {
            // Llamar al servicio de notificación asincrónica
            await notificationService.sendAsyncNotification(notificationData);
            console.log('Notificación procesada:', notificationData);

            // Confirmar que el mensaje ha sido procesado correctamente
            channel.ack(msg);
          } else {
            console.error('Mensaje con formato inválido:', notificationData);
            // No confirmar el mensaje si el formato es inválido
            channel.nack(msg, false, true); // Lo devuelve a la cola
          }
        } catch (error) {
          console.error('Error al procesar la notificación:', error);
          // Si hay un error al procesar, no confirmar el mensaje
          channel.nack(msg, false, true); // Lo devuelve a la cola
        }
      }
    });
  } catch (error) {
    console.error('Error de conexión a RabbitMQ:', error);
    setTimeout(connectRabbitMQ, 5000); // Reintentar la conexión cada 5 segundos si falla
  }
};

// Función para validar que el mensaje tenga la estructura correcta
const isValidNotification = (data) => {
  return (
    data &&
    Array.isArray(data.recipients) && data.recipients.length > 0 &&
    Array.isArray(data.channels) && data.channels.length > 0 &&
    data.message &&
    typeof data.message.subject === 'string' &&
    typeof data.message.body === 'string'
  );
};

// Intentamos conectarnos a RabbitMQ cuando la aplicación inicia
connectRabbitMQ();
