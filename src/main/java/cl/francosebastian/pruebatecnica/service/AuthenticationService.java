package cl.francosebastian.pruebatecnica.service;

import cl.francosebastian.pruebatecnica.configuration.CustomUserDetails;
import cl.francosebastian.pruebatecnica.dto.LoginDTO;
import cl.francosebastian.pruebatecnica.entity.UserEntity;
import cl.francosebastian.pruebatecnica.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Servicio para manejar la lógica de la autenticacion del usuario
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * @param input
     * @return
     */
    public UserEntity authenticate(LoginDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        UserEntity user = userRepository.findByCorreo(input.getEmail())
                .orElseThrow();
        user.setUltimoLogin(LocalDateTime.now());
        user.setToken(jwtService.generateToken(new CustomUserDetails(user)));
        userRepository.save(user);
        return user;
    }

}