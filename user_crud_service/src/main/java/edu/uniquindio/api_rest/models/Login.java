package edu.uniquindio.api_rest.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.*;


/**
 * Login
 */


public class Login {

  private String nombreUsuario;

  private String contraseña;

  public Login() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Login(String nombreUsuario, String contraseña) {
    this.nombreUsuario = nombreUsuario;
    this.contraseña = contraseña;
  }

  public Login nombreUsuario(String nombreUsuario) {
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

  public Login contraseña(String contraseña) {
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
    Login login = (Login) o;
    return Objects.equals(this.nombreUsuario, login.nombreUsuario) &&
        Objects.equals(this.contraseña, login.contraseña);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombreUsuario, contraseña);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Login {\n");
    sb.append("    nombreUsuario: ").append(toIndentedString(nombreUsuario)).append("\n");
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

