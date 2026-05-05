from pydantic import BaseModel
from datetime import datetime
from typing import Optional

class LogCreate(BaseModel):
    application_name: str
    log_type: str
    class_module: str
    summary: str
    description: str

class LogResponse(LogCreate):
    id: int
    created_at: datetime
    class Config:
        from_attributes = True

class HealthData(BaseModel):
    from_: datetime  # nombre a from_ para evitar conflictos con 'from' reservado en Python
    status: str

    class Config:
        fields = {"from_": "from"}  # Para que el JSON mantenga la clave "from"

class HealthCheck(BaseModel):
    name: str
    status: str
    data: HealthData

class HealthResponse(BaseModel):
    status: str
    checks: list[HealthCheck]