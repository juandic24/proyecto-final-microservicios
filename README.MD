# Proyecto Final — Arquitectura de Microservicios

Sistema distribuido desarrollado como proyecto universitario. Implementa una arquitectura de microservicios completa con autenticación JWT, mensajería asíncrona, observabilidad y orquestación con Docker Compose.

---

## Arquitectura

```
Cliente
   │
   ▼
API Gateway  :8090          ← punto de entrada único, valida JWT
   │
   ├── /api/auth/**         → user-crud-service  :8080  (Java / Spring Boot)
   ├── /api/usuarios/**     → user-crud-service  :8080
   ├── /api/notificaciones/**→ notification-service :3001 (Node.js / Express)
   ├── /api/logs/**         → logs-service        :8000  (Python / FastAPI)
   └── /api/monitoring/**   → monitoring-service  :3000  (Node.js / Express)

user-crud-service ──RabbitMQ──► notification-service ──SMTP──► Gmail
API Gateway       ──────────────────────────────────────────► logs-service
```

### Servicios

| Servicio | Tecnología | Puerto | Descripción |
|---|---|---|---|
| api-gateway | Node.js 18 / Express | 8090 | Entrada única, enrutamiento, validación JWT |
| user-crud-service | Java 21 / Spring Boot 3.3 | 8080 | Registro, login, recuperación de contraseña, CRUD de usuarios |
| notification-service | Node.js 18 / Express | 3001 | Consume cola RabbitMQ y envía emails via Gmail SMTP |
| logs-service | Python 3.12 / FastAPI | 8000 | Almacena logs de todos los servicios en PostgreSQL |
| monitoring-service | Node.js 18 / Express | 3000 | Health checks periódicos sobre los servicios |

### Infraestructura

| Componente | Imagen | Puerto |
|---|---|---|
| PostgreSQL (usuarios) | postgres:latest | 5432 |
| PostgreSQL (logs) | postgres:latest | 5433 |
| MongoDB (notificaciones) | mongo:latest | 27018 |
| MongoDB (monitoreo) | mongo:latest | 27017 |
| RabbitMQ | rabbitmq:management | 5672 / 15672 |

---

## Requisitos

- Docker Desktop (corriendo antes de ejecutar cualquier comando)
- Git

---

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/juandic24/proyecto-final-microservicios.git
cd proyecto-final-microservicios
```

### 2. Configurar variables de entorno

```bash
cp .env.example .env
```

Editar `.env` con los valores correspondientes:

```env
# PostgreSQL
POSTGRES_USER=usuario
POSTGRES_PASSWORD=contraseña
POSTGRES_DB_USERS=usersdb
POSTGRES_DB_LOGS=logsdb

# RabbitMQ
RABBITMQ_USER=admin
RABBITMQ_PASS=contraseña

# Email — requiere Gmail App Password
# Activar verificación en 2 pasos en la cuenta Gmail
# Generar App Password en: Seguridad → Contraseñas de aplicaciones
EMAIL_USER=tucorreo@gmail.com
EMAIL_PASS=xxxx xxxx xxxx xxxx

# JWT — cualquier string largo aleatorio (mínimo 32 caracteres, codificado en Base64)
JWT_SECRET=3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b
```

### 3. Levantar el proyecto

```bash
docker compose up
```

La primera vez tarda varios minutos porque Maven compila el servicio Java dentro de Docker. Las siguientes veces usa caché de capas y es mucho más rápido.

---

## Uso de la API

Todas las requests van al **API Gateway** en el puerto `8090`.

### Autenticación

#### Registrar usuario
```http
POST /api/auth/registro
Content-Type: application/json

{
  "nombre_usuario": "juan",
  "correo": "juan@ejemplo.com",
  "contraseña": "mipassword"
}
```

#### Iniciar sesión
```http
POST /api/auth/login
Content-Type: application/json

{
  "nombre_usuario": "juan",
  "contraseña": "mipassword"
}
```
Respuesta: `{ "token": "eyJ..." }`

#### Recuperar contraseña
```http
POST /api/auth/recuperacion-clave
Content-Type: application/json

{
  "correo": "juan@ejemplo.com"
}
```
Genera una contraseña temporal y la envía por email.

---

### CRUD de Usuarios *(requiere JWT)*

Incluir en todas las requests:
```
Authorization: Bearer <token>
```

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/usuarios/` | Listar usuarios (paginado) |
| `GET` | `/api/usuarios/{id}` | Obtener usuario por ID |
| `PUT` | `/api/usuarios/{id}` | Actualizar usuario |
| `DELETE` | `/api/usuarios/{id}` | Eliminar usuario |

---

### Logs *(requiere JWT)*

#### Ver logs registrados
```http
GET /api/logs/logs/?limit=20&application_name=user-crud-service
Authorization: Bearer <token>
```

Parámetros opcionales: `application_name`, `log_type` (INFO/WARNING/ERROR), `start_date`, `end_date`, `skip`, `limit`.

---

### Health checks

```http
GET /health                         # API Gateway
GET /api/monitoring/health          # Monitoring service (con JWT)
```

---

## Flujo de recuperación de contraseña

```
1. Cliente  →  POST /api/auth/recuperacion-clave  →  API Gateway
2. Gateway  →  user-crud-service
3. user-crud-service genera contraseña temporal
4. Publica mensaje en RabbitMQ (notifications_queue)
5. notification-service consume el mensaje
6. notification-service envía email via Gmail SMTP
7. Usuario recibe la contraseña temporal en su correo
```

---

## Documentación de APIs

Cada servicio expone su OpenAPI spec:

| Servicio | Swagger UI |
|---|---|
| user-crud-service | http://localhost:8080/swagger-ui.html |
| logs-service | http://localhost:8000/docs |
| notification-service | http://localhost:3001/api-docs |

---

## Decisiones de diseño relevantes

- **JWT centralizado en el gateway**: los servicios no validan tokens individualmente; el gateway actúa como único punto de autenticación.
- **Mensajería asíncrona**: el envío de email no bloquea la respuesta al cliente. Si RabbitMQ no está disponible, la contraseña no se cambia (consistencia ante fallos).
- **Logs fire-and-forget**: el gateway registra cada request en logs-service de forma asíncrona; si el servicio de logs falla, el request principal no se ve afectado.
- **Multi-stage build**: el servicio Java se compila dentro de Docker con Maven, eliminando dependencias externas en el host.
- **Healthchecks en Docker Compose**: PostgreSQL y RabbitMQ deben estar saludables antes de iniciar los servicios que dependen de ellos.

---

## Estructura del repositorio

```
proyecto-final-microservicios/
├── api_gateway/              # Node.js — enrutamiento y autenticación
├── user_crud_service/        # Java/Spring Boot — usuarios y auth
├── notification_service/     # Node.js — envío de emails
├── logs_service/             # Python/FastAPI — almacenamiento de logs
├── monitoring_service/       # Node.js — health checks
├── docker-compose.yaml       # Orquestación completa
└── .env.example              # Plantilla de variables de entorno
```
