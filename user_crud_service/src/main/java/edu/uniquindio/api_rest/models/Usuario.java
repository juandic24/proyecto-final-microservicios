package edu.uniquindio.api_rest.models;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import javax.validation.constraints.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * Usuario
 */

@Table(name = "users")
@Entity
public class Usuario implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer usuarioId;

  @Column(nullable = false, unique = true)
  private String nombreUsuario;

  @Column(nullable = false, unique = true)
  private String correo;

  @Column(nullable = false)
  private String contraseña;

  public Usuario() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Usuario(int usuarioId, String nombreUsuario, String correo) {
    this.usuarioId = usuarioId;
    this.nombreUsuario = nombreUsuario;
    this.correo = correo;
  }

  public Usuario(@NotNull String nombreUsuario2, @NotNull String correo2, @NotNull String contraseña2) {
    this.nombreUsuario =nombreUsuario2;
    this.correo = correo2;
    this.contraseña = contraseña2;
  }

  public Usuario usuarioId(int usuarioId) {
    this.usuarioId = usuarioId;
    return this;
  }

  /**
   * Get usuarioId
   * @return usuarioId
   */
  @NotNull 
  @JsonProperty("usuarioId")
  public int getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(int usuarioId) {
    this.usuarioId = usuarioId;
  }

  public Usuario nombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
    return this;
  }

  /**
   * Get nombreUsuario
   * @return nombreUsuario
   */
  @NotNull 
  @JsonProperty("nombre_usuario")
  public String getNombreUsuario() {
    return nombreUsuario;
  }

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }

  public Usuario correo(String correo) {
    this.correo = correo;
    return this;
  }

  /**
   * Get correo
   * @return correo
   */
  @NotNull 
  @JsonProperty("correo")
  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public Usuario contraseña(String contraseña) {
    this.contraseña = contraseña;
    return this;
  }

  /**
   * Get contraseña
   * @return contraseña
   */
  
  @JsonProperty("contraseña")
  public String getContraseña() {
    return contraseña;
  }

  public void setContraseña(String contraseña) {
    this.contraseña = contraseña;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Usuario usuario = (Usuario) o;
    return Objects.equals(this.usuarioId, usuario.usuarioId) &&
        Objects.equals(this.nombreUsuario, usuario.nombreUsuario) &&
        Objects.equals(this.correo, usuario.correo) &&
        Objects.equals(this.contraseña, usuario.contraseña);
  }

  @Override
  public int hashCode() {
    return Objects.hash(usuarioId, nombreUsuario, correo, contraseña);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Usuario {\n");
    sb.append("    usuarioId: ").append(toIndentedString(usuarioId)).append("\n");
    sb.append("    nombreUsuario: ").append(toIndentedString(nombreUsuario)).append("\n");
    sb.append("    correo: ").append(toIndentedString(correo)).append("\n");
    sb.append("    contraseña: ").append(toIndentedString(contraseña)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {

    return contraseña;
  }

  @Override
  public String getUsername() {
    return this.nombreUsuario;
  }

  @Override
    public boolean isAccountNonExpired() {
        return true;
    }

  @Override
  public boolean isAccountNonLocked() {
        return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
        return true;
  }

  @Override
  public boolean isEnabled() {
        return true;
  }
}

