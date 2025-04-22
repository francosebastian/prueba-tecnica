package cl.francosebastian.pruebatecnica.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Representacion del json de error requerido en la prueba
 */
@Data
@Builder
public class ErrorMessageDTO implements Serializable {

    private String mensaje;

}
