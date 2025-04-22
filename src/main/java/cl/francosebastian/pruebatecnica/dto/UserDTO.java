package cl.francosebastian.pruebatecnica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {

    private Long id;
    private String creado;
    private String correo;
    private String contrasena;
    private String ultimoLogin;
    private String token;
    private Boolean activo;
    private List<PhoneDTO> telefonos;
}
