package edu.uniquindio.api_rest.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.uniquindio.api_rest.services.HealthService;

import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    private final HealthService healthService;

    // Inyección de dependencias del servicio
    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    // Ruta general /health
    @GetMapping
    public Map<String, Object> healthCheck() {
        return healthService.getHealthStatus();
    }

    // Ruta de "Readiness" /health/ready
    @GetMapping("/ready")
    public Map<String, Object> readinessCheck() {
        return healthService.getReadinessStatus();
    }

    // Ruta de "Liveness" /health/live
    @GetMapping("/live")
    public Map<String, Object> livenessCheck() {
        return healthService.getLivenessStatus();
    }
}
