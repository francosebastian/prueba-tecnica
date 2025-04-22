package cl.francosebastian.pruebatecnica.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class PhoneEntity extends AutoincrementEntity {

    @Column
    private String numero;
    @Column
    private String codigoCiudad;
    @Column
    private String codigoPais;

}
