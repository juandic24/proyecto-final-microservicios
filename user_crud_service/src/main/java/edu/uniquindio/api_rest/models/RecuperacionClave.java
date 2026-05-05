package edu.uniquindio.api_rest.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.*;


/**
 * RecuperacionClave
 */

public class RecuperacionClave {

  private String correo;

  public RecuperacionClave() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RecuperacionClave(String correo) {
    this.correo = correo;
  }

  public RecuperacionClave correo(String correo) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RecuperacionClave recuperacionClave = (RecuperacionClave) o;
    return Objects.equals(this.correo, recuperacionClave.correo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(correo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RecuperacionClave {\n");
    sb.append("    correo: ").append(toIndentedString(correo)).append("\n");
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

