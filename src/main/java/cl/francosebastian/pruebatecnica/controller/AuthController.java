package cl.francosebastian.pruebatecnica.controller;

import cl.francosebastian.pruebatecnica.configuration.CustomUserDetails;
import cl.francosebastian.pruebatecnica.dto.LoginDTO;
import cl.francosebastian.pruebatecnica.dto.LoginResponseDTO;
import cl.francosebastian.pruebatecnica.entity.UserEntity;
import cl.francosebastian.pruebatecnica.service.AuthenticationService;
import cl.francosebastian.pruebatecnica.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de autenticacion
 */
@Log4j2
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    /**
     * Autentica a un usuario
     * @param loginUserDto DTO con username y password
     * @return Respuesta de autenticacion con token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginDTO loginUserDto) {
        UserEntity authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(new CustomUserDetails(authenticatedUser));
        LoginResponseDTO loginResponse = LoginResponseDTO.builder().token(jwtToken).expiresIn(jwtService.getExpirationTime()).build();
        return ResponseEntity.ok(loginResponse);
    }

}


