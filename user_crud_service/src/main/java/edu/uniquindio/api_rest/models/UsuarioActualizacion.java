package edu.uniquindio.api_rest.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UsuarioActualizacion
 */

public class UsuarioActualizacion {

  private String nombreUsuario;

  private String correo;

  private String contraseña;

  public UsuarioActualizacion nombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
    return this;
  }

  /**
   * Get nombreUsuario
   * @return nombreUsuario
   */
  
  @JsonProperty("nombre_usuario")
  public String getNombreUsuario() {
    return nombreUsuario;
  }

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }

  public UsuarioActualizacion correo(String correo) {
    this.correo = correo;
    return this;
  }

  /**
   * Get correo
   * @return correo
   */
  
  @JsonProperty("correo")
  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public UsuarioActualizacion contraseña(String contraseña) {
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
    UsuarioActualizacion usuarioActualizacion = (UsuarioActualizacion) o;
    return Objects.equals(this.nombreUsuario, usuarioActualizacion.nombreUsuario) &&
        Objects.equals(this.correo, usuarioActualizacion.correo) &&
        Objects.equals(this.contraseña, usuarioActualizacion.contraseña);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombreUsuario, correo, contraseña);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UsuarioActualizacion {\n");
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
}

