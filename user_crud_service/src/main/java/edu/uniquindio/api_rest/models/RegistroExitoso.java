package edu.uniquindio.api_rest.models;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RegistroExitoso
 */

public class RegistroExitoso {

  private String message;

  private int usuarioId;

  public RegistroExitoso (String message, @NotNull int usuarioId) {
    this.message = message;
    this.usuarioId = usuarioId;
  }

  public RegistroExitoso message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public RegistroExitoso usuarioId(int usuarioId) {
    this.usuarioId = usuarioId;
    return this;
  }

  /**
   * Get usuarioId
   * @return usuarioId
   */
  
  @JsonProperty("usuarioId")
  public int getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(int usuarioId) {
    this.usuarioId = usuarioId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegistroExitoso registroExitoso = (RegistroExitoso) o;
    return Objects.equals(this.message, registroExitoso.message) &&
        Objects.equals(this.usuarioId, registroExitoso.usuarioId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, usuarioId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegistroExitoso {\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    usuarioId: ").append(toIndentedString(usuarioId)).append("\n");
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

