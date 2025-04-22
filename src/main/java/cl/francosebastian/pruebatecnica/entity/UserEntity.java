package cl.francosebastian.pruebatecnica.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name="users")
public class UserEntity extends AutoincrementEntity {

    @Column(unique = true)
    private String correo;
    @Column
    private String password;
    @Column
    private String token;
    @Column
    private Boolean activo;
    @Column
    private LocalDateTime ultimoLogin;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneEntity> telefonos;

}
