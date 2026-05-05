import os
from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker
import models


DATABASE_URL = os.getenv('DB_URL')

# Verificar que la URL está disponible
if DATABASE_URL is None:
    raise ValueError("La variable de entorno DATABASE_URL no está definida")

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# Crear tablas
models.Base.metadata.create_all(bind=engine)

def check_database_connection():
    try:
        with engine.connect() as conn:
            conn.execute(text("SELECT 1"))  # Prueba de consulta para verificar la conexión
        return True
    except Exception as e:
        print(f"Database connection failed: {e}")
        return False
