package edu.uniquindio.api_rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) // Devuelve 503
    public Map<String, String> handleServiceUnavailableException(ServiceUnavailableException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "DOWN");
        response.put("message", ex.getMessage());
        return response;
    }
}
