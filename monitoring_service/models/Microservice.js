const mongoose = require('mongoose');

const microserviceSchema = new mongoose.Schema({
  name: { type: String, required: true, unique: true },
  endpoint: { type: String, required: true },
  frequency: { type: Number, required: true },
  emails: [{ type: String, required: true }],
  lastNotified: { type: Date, default: null }
});

module.exports = mongoose.model('Microservice', microserviceSchema);
