package edu.uniquindio.api_rest.controllers;

import edu.uniquindio.api_rest.models.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import edu.uniquindio.api_rest.services.AuthenticationService;



@RestController

public class AuthenticationController {
      private final AuthenticationService authenticationService;
      
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    //Login
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody Login login) {
        return authenticationService.authenticate(login);
    }

    //Recuperación contraseña 
    @PostMapping("/recuperacion-clave")
    public ResponseEntity<?> recuperarClave(@RequestBody RecuperacionClave recuperacionClave) {
        return authenticationService.recuperarContraseña(recuperacionClave);
    }



}
