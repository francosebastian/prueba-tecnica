package cl.francosebastian.pruebatecnica.handler;

import cl.francosebastian.pruebatecnica.dto.ErrorMessageDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * @author Franco
 */
@Hidden
@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * SpringSecurityException type
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessageDTO> handleBadCredentialsException(HttpServletRequest req,
                                                                       BadCredentialsException ex) {
        log.error("Error capturado BadCredentialsException : ", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessageDTO.builder().mensaje("Credenciales no validas").build());
    }

    /**
     * SpringSecurityException type
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessageDTO> handleAccessDeniedException(HttpServletRequest req,
                                                                     AccessDeniedException ex) {
        log.error("Error capturado AccessDeniedException : ", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessageDTO.builder().mensaje("Acceso denegado").build());
    }

    /**
     * SpringSecurityException type
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessageDTO> handleAuthenticationException(HttpServletRequest req,
                                                                       AuthenticationException ex) {
        log.error("Error capturado AuthenticationException : ", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessageDTO.builder().mensaje("No autorizado").build());
    }

    /**
     * JwtException type
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorMessageDTO> handleExpiredJwtException(HttpServletRequest req,
                                                                   ExpiredJwtException ex) {
        log.error("Error capturado ExpiredJwtException : ", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessageDTO.builder().mensaje("Token Expirado").build());
    }

    /**
     * JwtException type
     */
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorMessageDTO> handleMalformedJwtException(HttpServletRequest req,
                                                                     MalformedJwtException ex) {
        log.error("Error capturado MalformedJwtException: ", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessageDTO.builder().mensaje("Jwt mal formado").build());
    }

    /**
     * Handle all exceptions not declared
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDTO> handleGlobalException(WebRequest req, Exception ex) {
        log.error("Error capturado Exception : ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessageDTO.builder().mensaje("Error al procesar").build());
    }


}
