package edu.uniquindio.api_rest.controllers;
import edu.uniquindio.api_rest.models.*;
import edu.uniquindio.api_rest.services.*;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UserController {
     
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Registro de un nuevo usuario
    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioRegistro usuarioRegistro) {
        return userService.registrarUsuario(usuarioRegistro);
    }

    // Obtener detalles del usuario por ID
    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable int usuarioId) {
        return userService.obtenerUsuario(usuarioId);
    }

    // Actualizar detalles del usuario
    @PutMapping("/{usuarioId}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable int usuarioId, @RequestBody UsuarioActualizacion usuarioActualizacion) {
        return userService.actualizarUsuario(usuarioId, usuarioActualizacion);
    }

    // Eliminar un usuario por ID
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable int usuarioId) {
        return userService.eliminarUsuario(usuarioId);
    }

    // Listar todos los usuarios -paginado
    @GetMapping("/")
    public Page<Usuario> obtenerLista() {
        int page = 2;
        int size = 10;
        return userService.obtenerLista(page,size);
    }

}