package edu.uniquindio.api_rest.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import edu.uniquindio.api_rest.models.Error;
import edu.uniquindio.api_rest.models.Exito;
import edu.uniquindio.api_rest.models.RegistroExitoso;
import edu.uniquindio.api_rest.models.Usuario;
import edu.uniquindio.api_rest.models.UsuarioActualizacion;
import edu.uniquindio.api_rest.models.UsuarioRegistro;
import edu.uniquindio.api_rest.repositories.*;
import org.springframework.security.core.Authentication;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Registro de usuario
    public ResponseEntity<?> registrarUsuario(UsuarioRegistro usuarioRegistro) {
        // Verificar si el usuario ya existe
        if (userRepository.existsByCorreo(usuarioRegistro.getCorreo())
                || userRepository.existsByNombreUsuario(usuarioRegistro.getNombreUsuario())) {
            // Devolver error 409 si el usuario ya existe
            Error error = new Error("El usuario ya existe");
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }

        // Validar datos de entrada
        if (usuarioRegistro.getCorreo() == null || usuarioRegistro.getContraseña() == null
                || usuarioRegistro.getNombreUsuario() == null) {
            // Devolver error 400 si hay datos inválidos
            Error error = new Error("Entrada inválida, falta información.");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        // Crear nuevo usuario
        Usuario nuevoUsuario = new Usuario(usuarioRegistro.getNombreUsuario(), usuarioRegistro.getCorreo(),
                usuarioRegistro.getContraseña());
        nuevoUsuario.setContraseña(passwordEncoder.encode(nuevoUsuario.getContraseña()));
        userRepository.save(nuevoUsuario);

        // Registro exitoso
        RegistroExitoso registroExitoso = new RegistroExitoso("Usuario registrado con éxito",
                nuevoUsuario.getUsuarioId());
        return new ResponseEntity<>(registroExitoso, HttpStatus.CREATED);
    }

    // Obtener usuario
    public ResponseEntity<?> obtenerUsuario(int usuarioId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Obtener usuario
                                                                                                // autenticado

        // Verificar si hay un usuario autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            Error error = new Error("No autorizado.");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        // Obtener el usuario autenticado
        Usuario usuario;
        try {
            usuario = (Usuario) authentication.getPrincipal();
        } catch (ClassCastException e) {
            Error error = new Error("No autorizado.");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        // Verificar si el usuario autenticado existe
        if (!userRepository.existsById(usuario.getUsuarioId())) {
            Error error = new Error("Usuario no encontrado.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        // Verificar si el usuario autenticado es el mismo que el usuario solicitado
        if (usuario.getUsuarioId() == usuarioId) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            // Verificar si el usuario solicitado existe
            if (userRepository.existsById(usuarioId)) {
                Error error = new Error("No autorizado.");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            } else {
                Error error = new Error("Usuario no encontrado.");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        }
    }

    // Actualizar usuario
    public ResponseEntity<?> actualizarUsuario(int usuarioId, UsuarioActualizacion usuarioActualizacion) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Obtener usuario
                                                                                                // autenticado

        // Verificar si hay un usuario autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            Error error = new Error("No autorizado.");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        // Obtener el usuario autenticado
        Usuario usuario;
        try {
            usuario = (Usuario) authentication.getPrincipal();
        } catch (ClassCastException e) {
            Error error = new Error("No autorizado.");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        // Verificar si los datos faltan
        if (usuarioActualizacion.getNombreUsuario() == null && usuarioActualizacion.getCorreo() == null
                && usuarioActualizacion.getContraseña() == null) {
            Error error = new Error("Datos faltantes.");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        // Si el usuario autenticado es el mismo que el usuario solicitado
        if (usuario.getUsuarioId() == usuarioId) {
            // Actualizar datos del usuario autenticado
            if (usuarioActualizacion.getNombreUsuario() != null) {
                usuario.setNombreUsuario(usuarioActualizacion.getNombreUsuario());
            }
            if (usuarioActualizacion.getCorreo() != null) {
                usuario.setCorreo(usuarioActualizacion.getCorreo());
            }
            if (usuarioActualizacion.getContraseña() != null) {
                // Encriptar la contraseña antes de guardarla
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String contraseñaEncriptada = passwordEncoder.encode(usuarioActualizacion.getContraseña());
                usuario.setContraseña(contraseñaEncriptada);
            }
            // Guardar los cambios en la base de datos
            userRepository.save(usuario);
            Exito exito = new Exito("Usuario actualizado con éxito.");
            return new ResponseEntity<>(exito, HttpStatus.OK);
        } else if (!userRepository.existsById(usuarioId)) { // Si el usuario solicitado no existe
            Error error = new Error("Usuario no encontrado.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } else {
            Error error = new Error("No autorizado."); // Si el usuario autenticado no coincide con el solicitado
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }

    // Eliminar usuario
    public ResponseEntity<?> eliminarUsuario(int usuarioId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Obtener usuario
                                                                                                // autenticado

        // Verificar si hay un usuario autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            Error error = new Error("No autorizado.");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        // Obtener el usuario autenticado
        Usuario usuario;
        try {
            usuario = (Usuario) authentication.getPrincipal();
        } catch (ClassCastException e) {
            Error error = new Error("No autorizado.");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        // Verificar si el usuario autenticado es el mismo que el usuario solicitado
        if (usuario.getUsuarioId() == usuarioId) {
            userRepository.delete(usuario);
            Exito exito = new Exito("Usuario eliminado con éxito.");
            return new ResponseEntity<>(exito, HttpStatus.OK);
        } else {
            // Verificar si el usuario solicitado existe
            if (userRepository.existsById(usuarioId)) {
                Error error = new Error("No autorizado.");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            } else {
                Error error = new Error("Usuario no encontrado.");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        }
    }

    public Page<Usuario> obtenerLista(int page, int size) {
        // TODO Auto-generated method stub
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

}
