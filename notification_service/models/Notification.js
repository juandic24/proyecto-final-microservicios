const mongoose = require('mongoose');

const NotificationSchema = new mongoose.Schema({
  recipients: {
    type: [String],
    required: true
  },
  state: {
    type: String,
    default: 'pending'
  },
  channels: {
    type: [String],
    required: true
  },
  message: {
    subject: String,
    body: String
  },
  sentAt: {
    type: Date
  }
});

module.exports = mongoose.model('Notification', NotificationSchema);
