Servicio de Logs hecho en Python con 
FastAPI y SQLAlchemy


Tabla de logs:

CREATE TABLE logs (
    id SERIAL PRIMARY KEY,
    application_name VARCHAR(255) NOT NULL,
    log_type VARCHAR(50) NOT NULL,
    module VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    summary TEXT,
    description TEXT
);
