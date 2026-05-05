package edu.uniquindio.api_rest.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.*;


/**
 * UsuarioRegistro
 */

public class UsuarioRegistro {

  private String nombreUsuario;

  private String correo;

  private String contraseña;

  public UsuarioRegistro() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UsuarioRegistro(String nombreUsuario, String correo, String contraseña) {
    this.nombreUsuario = nombreUsuario;
    this.correo = correo;
    this.contraseña = contraseña;
  }

  public UsuarioRegistro nombreUsuario(String nombreUsuario) {
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

  public UsuarioRegistro correo(String correo) {
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

  public UsuarioRegistro contraseña(String contraseña) {
    this.contraseña = contraseña;
    return this;
  }

  /**
   * Get contraseña
   * @return contraseña
   */
  @NotNull 
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
    UsuarioRegistro usuarioRegistro = (UsuarioRegistro) o;
    return Objects.equals(this.nombreUsuario, usuarioRegistro.nombreUsuario) &&
        Objects.equals(this.correo, usuarioRegistro.correo) &&
        Objects.equals(this.contraseña, usuarioRegistro.contraseña);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombreUsuario, correo, contraseña);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UsuarioRegistro {\n");
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



