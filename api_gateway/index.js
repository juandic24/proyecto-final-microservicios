const express = require('express');
const http = require('http');
const jwt = require('jsonwebtoken');
const { URL } = require('url');

const app = express();
const PORT = process.env.PORT || 8090;
const JWT_SECRET = Buffer.from(process.env.JWT_SECRET, 'base64');

const SERVICES = {
  userCrud:     'http://user-crud-service:8080',
  notification: 'http://notification-service:3001',
  logs:         'http://logs-service:8000',
  monitoring:   'http://monitoring-service:3000',
};

const HOP_BY_HOP = new Set([
  'connection', 'keep-alive', 'proxy-authenticate', 'proxy-authorization',
  'te', 'trailers', 'transfer-encoding', 'upgrade',
]);

// Envía un log al logs-service de forma asíncrona (fire-and-forget).
// Si el logs-service está caído, el error se descarta silenciosamente.
function sendLog(serviceName, statusCode, method, originalUrl) {
  const logType = statusCode >= 500 ? 'ERROR' : statusCode >= 400 ? 'WARNING' : 'INFO';
  const data = JSON.stringify({
    application_name: serviceName,
    log_type: logType,
    class_module: 'api-gateway',
    summary: `${method} ${originalUrl} → ${statusCode}`,
    description: `Request proxied to ${serviceName}`,
  });

  const logsUrl = new URL(SERVICES.logs);
  const req = http.request({
    hostname: logsUrl.hostname,
    port: logsUrl.port || 80,
    path: '/logs/',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Content-Length': Buffer.byteLength(data),
    },
  });
  req.on('error', () => {}); // descarta errores — el logging no debe afectar el request principal
  req.write(data);
  req.end();
}

function proxyTo(target, pathOverride, serviceName) {
  const url = new URL(target);
  return (req, res) => {
    const path = pathOverride !== undefined ? pathOverride(req.url) : req.url;

    const headers = {};
    for (const [k, v] of Object.entries(req.headers)) {
      if (!HOP_BY_HOP.has(k.toLowerCase())) headers[k] = v;
    }
    headers['host'] = url.host;

    const options = {
      hostname: url.hostname,
      port: url.port || 80,
      path,
      method: req.method,
      headers,
    };

    const proxyReq = http.request(options, (proxyRes) => {
      const statusCode = proxyRes.statusCode;
      res.writeHead(statusCode, proxyRes.headers);
      proxyRes.pipe(res);
      // Al terminar de enviar la respuesta, registrar el log
      proxyRes.on('end', () => sendLog(serviceName, statusCode, req.method, req.originalUrl));
    });

    proxyReq.on('error', (err) => {
      console.error(`[PROXY ERROR] ${req.method} ${req.originalUrl} →`, err.message);
      if (!res.headersSent) res.status(502).json({ error: 'Servicio no disponible.' });
    });

    req.pipe(proxyReq);
  };
}

function logger(req, _res, next) {
  console.log(`[${new Date().toISOString()}] ${req.method} ${req.originalUrl}`);
  next();
}

function authenticate(req, res, next) {
  const authHeader = req.headers['authorization'];
  if (!authHeader?.startsWith('Bearer ')) {
    return res.status(401).json({ error: 'Token no proporcionado.' });
  }
  try {
    jwt.verify(authHeader.split(' ')[1], JWT_SECRET, { algorithms: ['HS256'] });
    next();
  } catch {
    return res.status(401).json({ error: 'Token inválido o expirado.' });
  }
}

app.use(logger);

app.get('/health', (_req, res) => res.json({ status: 'ok' }));

app.use('/api/auth/registro', proxyTo(SERVICES.userCrud, () => '/usuarios', 'user-crud-service'));
app.use('/api/auth',          proxyTo(SERVICES.userCrud, undefined, 'user-crud-service'));
app.use('/api/usuarios',      authenticate, proxyTo(SERVICES.userCrud, (p) => `/usuarios${p}`, 'user-crud-service'));
app.use('/api/notificaciones',authenticate, proxyTo(SERVICES.notification, undefined, 'notification-service'));
app.use('/api/logs',          authenticate, proxyTo(SERVICES.logs, undefined, 'logs-service'));
app.use('/api/monitoring',    authenticate, proxyTo(SERVICES.monitoring, undefined, 'monitoring-service'));

app.listen(PORT, () => console.log(`API Gateway corriendo en puerto ${PORT}`));
