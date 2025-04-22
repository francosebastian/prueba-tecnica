package cl.francosebastian.pruebatecnica.exception;

import cl.francosebastian.pruebatecnica.dto.ErrorMessageDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ErrorMessageDTO> emailException(HttpServletRequest req,
                                                                         EmailException ex) {
        log.error("Error capturado EmailException : ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessageDTO.builder().mensaje("Email no valido").build());
    }

    /**
     * SpringSecurityException type
     */
    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<ErrorMessageDTO> passwordException(HttpServletRequest req,
                                                          PasswordException ex) {
        log.error("Error capturado PasswordException : ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessageDTO.builder().mensaje("Password no cumple politica de seguridad.").build());
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
     * SpringSecurityException type
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessageDTO> handleConstraintViolationException(HttpServletRequest req,
                                                                              DataIntegrityViolationException ex) {
        log.error("Error capturado DataIntegrityViolationException : ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessageDTO.builder().mensaje("Email ya existe en la DB").build());
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
