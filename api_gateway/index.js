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

// Hop-by-hop headers que no se deben reenviar al servicio destino
const HOP_BY_HOP = new Set([
  'connection', 'keep-alive', 'proxy-authenticate', 'proxy-authorization',
  'te', 'trailers', 'transfer-encoding', 'upgrade',
]);

function proxyTo(target, pathOverride) {
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
      res.writeHead(proxyRes.statusCode, proxyRes.headers);
      proxyRes.pipe(res);
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

// Público: registro → POST /usuarios en user-crud-service
app.use('/api/auth/registro', proxyTo(SERVICES.userCrud, () => '/usuarios'));

// Público: login y recuperacion-clave
// Express stripea /api/auth → req.url queda como /login o /recuperacion-clave
app.use('/api/auth', proxyTo(SERVICES.userCrud));

// Protegido: CRUD de usuarios
// Express stripea /api/usuarios → req.url queda como / o /:id
// Reescribir agregando /usuarios de vuelta
app.use('/api/usuarios', authenticate, proxyTo(SERVICES.userCrud, (path) => `/usuarios${path}`));

// Protegido: demás servicios — path restante se reenvía directo
app.use('/api/notificaciones', authenticate, proxyTo(SERVICES.notification));
app.use('/api/logs',           authenticate, proxyTo(SERVICES.logs));
app.use('/api/monitoring',     authenticate, proxyTo(SERVICES.monitoring));

app.listen(PORT, () => console.log(`API Gateway corriendo en puerto ${PORT}`));
