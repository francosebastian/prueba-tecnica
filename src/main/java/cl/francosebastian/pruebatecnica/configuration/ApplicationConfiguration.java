package cl.francosebastian.pruebatecnica.configuration;


import cl.francosebastian.pruebatecnica.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuraciones de context de usuario de spring security
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private final CustomUserDetailService customUserDetailService;

    /**
     * Encriptador de contraseñas para no exponerlas en texto plano en la DB
     *
     * @return Instancia de encriptador inyectada en java bean
     */
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el gestor de autenticación utilizando la configuración de Spring Security.
     *
     * @param config Configuración de autenticación de Spring Security.
     * @return Instancia de AuthenticationManager.
     * @throws Exception Si ocurre un error al obtener el AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configura el proveedor de autenticación utilizando DAO y codificación de contraseña.
     *
     * @return Una instancia de AuthenticationProvider.
     */
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}