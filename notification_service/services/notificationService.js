const nodemailer = require('nodemailer');
const Notification = require('../models/Notification'); // Asegúrate de que Notification sea un modelo de Mongoose
require('dotenv').config();

class NotificationService {
  // Método sincrónico para enviar la notificación
  async sendSyncNotification(notification) {
    try {
      const transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
          user: process.env.EMAIL_USER,
          pass: process.env.EMAIL_PASS
        }
      });

      const mailOptions = {
        from: process.env.EMAIL_USER,
        to: notification.recipients.join(','),
        subject: notification.message.subject,
        text: notification.message.body,
      };

      const info = await transporter.sendMail(mailOptions);
      notification.state = 'sent';
      notification.sentAt = new Date();

      // Crear una nueva instancia de Notification (Modelo de Mongoose)
      const notificationInstance = new Notification(notification); 
      await notificationInstance.save();  // Guardar la instancia en la base de datos

      return info;
    } catch (error) {
      console.error('Error sending email:', error);
      notification.state = 'failed';

      // Guardar el estado fallido
      const notificationInstance = new Notification(notification);
      await notificationInstance.save();  // Guardar el estado fallido

      throw error;
    }
  }

  // Método asincrónico para enviar la notificación
  async sendAsyncNotification(notification) {
    try {
      const transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
          user: process.env.EMAIL_USER,
          pass: process.env.EMAIL_PASS
        }
      });

      const mailOptions = {
        from: process.env.EMAIL_USER,
        to: notification.recipients.join(','),
        subject: notification.message.subject,
        text: notification.message.body,
      };

      await transporter.sendMail(mailOptions);
      notification.state = 'sent';
      notification.sentAt = new Date();

      // Crear una nueva instancia de Notification (Modelo de Mongoose)
      const notificationInstance = new Notification(notification); 
      await notificationInstance.save();  // Guardar la instancia en la base de datos
    } catch (error) {
      console.error('Error sending email:', error);
      notification.state = 'failed';

      // Guardar el estado fallido
      const notificationInstance = new Notification(notification);
      await notificationInstance.save();  // Guardar el estado fallido
    }
  }
}

module.exports = new NotificationService();
