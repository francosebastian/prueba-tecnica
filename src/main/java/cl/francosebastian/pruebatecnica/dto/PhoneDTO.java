package cl.francosebastian.pruebatecnica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneDTO implements Serializable {

    private String numero;
    private String codigoCiudad;
    private String codigoPais;

}
