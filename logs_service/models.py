from sqlalchemy import Column, String, DateTime, Integer
from sqlalchemy.ext.declarative import declarative_base
import datetime

Base = declarative_base()

class Log(Base):
    __tablename__ = "logs"
    id = Column(Integer, primary_key=True, index=True)
    application_name = Column(String, index=True)
    log_type = Column(String, index=True)
    class_module = Column(String)
    created_at = Column(DateTime, default=datetime.datetime.utcnow)
    summary = Column(String)
    description = Column(String)
