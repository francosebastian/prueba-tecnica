package cl.francosebastian.pruebatecnica.service;

import cl.francosebastian.pruebatecnica.configuration.CustomUserDetails;
import cl.francosebastian.pruebatecnica.dto.CreateUserDTO;
import cl.francosebastian.pruebatecnica.dto.PhoneDTO;
import cl.francosebastian.pruebatecnica.dto.UserDTO;
import cl.francosebastian.pruebatecnica.entity.PhoneEntity;
import cl.francosebastian.pruebatecnica.entity.UserEntity;
import cl.francosebastian.pruebatecnica.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Servicio para manejar la lógica de negocio de los usuarios.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Value("${password.pattern}")
    private String passwordRegex;

    private final UserRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Obtiene la lista de todos los usuarios registrados.
     *
     * @return Lista de objetos UsuarioDTO con la información de cada usuario.
     */
    public List<UserDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream().map(this::toDTO).toList();
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id Identificador del usuario.
     * @return Un Optional que contiene el UsuarioDTO si se encuentra, o vacío si no existe.
     */
    public Optional<UserDTO> getUsuarioById(Long id) {
        return usuarioRepository.findById(id).map(this::toDTO);
    }

    /**
     * Crea un nuevo usuario en la base de datos.
     *
     * @param usuarioDTO Datos del usuario a crear.
     * @return El UsuarioDTO creado con su ID asignado.
     * @throws RuntimeException si el tipo de usuario o la empresa no existen.
     */
    public UserDTO createUsuario(CreateUserDTO usuarioDTO) {

        if(!Pattern.compile(EMAIL_REGEX)
                .matcher(usuarioDTO.getCorreo())
                .matches())
            throw new RuntimeException("Correo no valido");

        if(!Pattern.compile(passwordRegex)
                .matcher(usuarioDTO.getContrasena())
                .matches())
            throw new RuntimeException("Password no valido");

        UserEntity user = new UserEntity();
        user.setPassword(passwordEncoder.encode(usuarioDTO.getContrasena()));
        user.setCorreo(usuarioDTO.getCorreo());
        user.setUltimoLogin(LocalDateTime.now());
        user.setActivo(true);
        user.setToken(jwtService.generateToken(new CustomUserDetails(user)));

        List<PhoneEntity> phones = new ArrayList<>();

        for(PhoneDTO phoneDTO: usuarioDTO.getTelefonos()){
            PhoneEntity phoneEntity = new PhoneEntity();
            phoneEntity.setNumero(phoneDTO.getNumero());
            phoneEntity.setCodigoPais(phoneDTO.getCodigoPais());
            phoneEntity.setCodigoCiudad(phoneDTO.getCodigoCiudad());
            phones.add(phoneEntity);
        }

        user.setTelefonos(phones);
        return toDTO(usuarioRepository.save(user));
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param id             Identificador del usuario a actualizar.
     * @param usuarioDetails Datos actualizados del usuario.
     * @return Un Optional que contiene el UsuarioDTO actualizado, o vacío si el usuario no existe.
     * @throws RuntimeException si el tipo de usuario o la empresa no existen.
     */
    public Optional<UserDTO> updateUsuario(Long id, UserDTO usuarioDetails) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setPassword(passwordEncoder.encode(usuarioDetails.getContrasena()));
            usuario.setActivo(usuarioDetails.getActivo());
            return toDTO(usuarioRepository.save(usuario));
        });
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id Identificador del usuario a eliminar.
     * @return true si se eliminó correctamente, false si el usuario no existe.
     */
    public boolean deleteUsuario(Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuarioRepository.delete(usuario);
            return true;
        }).orElse(false);
    }

    /**
     * Convierte una entidad Usuario en un DTO.
     *
     * @param usuario Entidad Usuario a convertir.
     * @return UsuarioDTO con la información de la entidad proporcionada.
     */
    private UserDTO toDTO(UserEntity usuario) {
        UserDTO usuarioDTO = new UserDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setActivo(usuario.getActivo());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setToken(usuario.getToken());
        usuarioDTO.setUltimoLogin(String.valueOf(usuario.getUltimoLogin()));
        usuarioDTO.setCreado(String.valueOf(usuario.getCreatedDate()));

        List<PhoneDTO> telefonos = new ArrayList<>();
        for(PhoneEntity phoneEntity: usuario.getTelefonos()){
            PhoneDTO phoneDTO = PhoneDTO.builder()
                    .codigoCiudad(phoneEntity.getCodigoCiudad())
                    .codigoPais(phoneEntity.getCodigoPais())
                    .numero(phoneEntity.getNumero())
                    .build();
            telefonos.add(phoneDTO);
        }


        usuarioDTO.setTelefonos(telefonos);

        return usuarioDTO;
    }
}