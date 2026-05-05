package edu.uniquindio.api_rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;


import java.util.HashMap;
import java.util.Map;

@Service
public class HealthService {

    private final LocalDateTime startTime = LocalDateTime.now();
    private final String version = "1.0.0"; 

    @Autowired
    private JdbcTemplate jdbcTemplate; // Para verificar la conexión a la base de datos


    // Método que devuelve la información general de salud
    public Map<String, Object> getHealthStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", isServiceUp() ? "UP" : "DOWN");
        response.put("checks", new Map[]{
            getReadinessStatus(),
            getLivenessStatus()
        });
        return response;
    }

    // Verificación de disponibilidad (Readiness) con verificación de dependencias
    public Map<String, Object> getReadinessStatus() {
        Map<String, Object> readiness = new HashMap<>();
        readiness.put("name", "Readiness check");
        readiness.put("status", isDatabaseUp() ? "UP" : "DOWN");

        Map<String, Object> data = new HashMap<>();
        data.put("from", startTime.toString());
        data.put("status", isDatabaseUp() ? "READY" : "NOT READY");
        readiness.put("data", data);

        return readiness;
    }

    // Verificación de actividad (Liveness), sólo verifica que el servicio esté activo
    public Map<String, Object> getLivenessStatus() {
        Map<String, Object> liveness = new HashMap<>();
        liveness.put("name", "Liveness check");
        liveness.put("status", "UP");

        Map<String, Object> data = new HashMap<>();
        data.put("from", startTime.toString());
        data.put("status", "ALIVE");
        liveness.put("data", data);

        return liveness;
    }

 
    // Método para comprobar si la base de datos está accesible
    private boolean isDatabaseUp() {
        try {
            jdbcTemplate.execute("SELECT 1"); // Consulta simple para probar la conexión
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    // Verifica si el servicio principal está activo (Readiness y Liveness)
    private boolean isServiceUp() {
        return isDatabaseUp();
    }

}