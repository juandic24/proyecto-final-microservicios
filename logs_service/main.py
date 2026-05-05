from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List, Optional
from datetime import datetime, timezone
import models
import database
import schemas 
import uvicorn


app = FastAPI()

# Tiempo de inicio del servicio
start_time = datetime.now(timezone.utc)

# Información de versión del servicio
SERVICE_VERSION = "1.0.0"

def get_uptime():
    return datetime.now(timezone.utc) - start_time

# Dependencia de la base de datos
def get_db():
    db = database.SessionLocal()
    try:
        yield db
    finally:
        db.close()

# Endpoint para crear un log
@app.post("/logs/", response_model=schemas.LogResponse)
def create_log(log: schemas.LogCreate, db: Session = Depends(get_db)):
    db_log = models.Log(**log.model_dump())
    db.add(db_log)
    db.commit()
    db.refresh(db_log)
    return db_log

# Endpoint para obtener logs con filtros y paginación
@app.get("/logs/", response_model=List[schemas.LogResponse])
def get_logs(
    db: Session = Depends(get_db),
    application_name: Optional[str] = None,
    log_type: Optional[str] = None,
    start_date: Optional[datetime] = None,
    end_date: Optional[datetime] = None,
    skip: int = 0,
    limit: int = 10
):
    query = db.query(models.Log)
    if application_name:
        query = query.filter(models.Log.application_name == application_name)
    if log_type:
        query = query.filter(models.Log.log_type == log_type)
    if start_date:
        query = query.filter(models.Log.created_at >= start_date)
    if end_date:
        query = query.filter(models.Log.created_at <= end_date)
    return query.order_by(models.Log.created_at).offset(skip).limit(limit).all()

@app.get("/health", response_model=schemas.HealthResponse)
def health_check():
    # Check the database connection for readiness
    db_ready = database.check_database_connection()
    readiness_status = "READY" if db_ready else "NOT READY"
    
    response = schemas.HealthResponse(
        status="UP" if db_ready else "DOWN",
        checks=[
            schemas.HealthCheck(
                name="Readiness check",
                status="UP" if db_ready else "DOWN",
                data=schemas.HealthData(
                    from_=start_time,
                    status=readiness_status
                )
            ),
            schemas.HealthCheck(
                name="Liveness check",
                status="UP",
                data=schemas.HealthData(
                    from_=start_time,
                    status="ALIVE"
                )
            )
        ]
    )
    return response

@app.get("/health/ready")
def readiness_check():
    # Devuelve "READY" solo si la base de datos está disponible
    db_ready = database.check_database_connection()
    if not db_ready:
        return {
            "status": "DOWN",
            "version": SERVICE_VERSION,
            "uptime": str(get_uptime())
        }
    return {
        "status": "UP",
        "version": SERVICE_VERSION,
        "uptime": str(get_uptime())
    }

@app.get("/health/live")
def liveness_check():
    return {
        "status": "UP",
        "version": SERVICE_VERSION,
        "uptime": str(get_uptime())
    }


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
