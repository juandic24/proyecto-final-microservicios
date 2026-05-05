package edu.uniquindio.api_rest.repositories;

import java.util.*;

import javax.validation.constraints.NotNull;

import edu.uniquindio.api_rest.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByCorreo(@NotNull String correo);
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByUsuarioId(int usuarioId);
    Optional<Usuario> findBynombreUsuario(String nombreUsuario);
    boolean existsByNombreUsuario(@NotNull String nombreUsuario);

}


