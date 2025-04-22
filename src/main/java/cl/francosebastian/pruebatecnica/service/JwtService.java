package cl.francosebastian.pruebatecnica.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Servicio para manejar la lógica del token jwt.
 */
@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Extrae el nombre de usuario de un token JWT.
     *
     * @param token el token JWT
     * @return el nombre de usuario extraído
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae una reclamación específica del token JWT.
     *
     * @param token          el token JWT
     * @param claimsResolver la función que define cómo extraer una reclamación
     * @param <T>            el tipo de la reclamación
     * @return el valor de la reclamación extraída
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Genera un token JWT para un usuario específico.
     *
     * @param userDetails los detalles del usuario
     * @return el token JWT generado
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token JWT con reclamaciones adicionales.
     *
     * @param extraClaims las reclamaciones adicionales
     * @param userDetails los detalles del usuario
     * @return el token JWT generado
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Obtiene el tiempo de expiración del JWT.
     *
     * @return el tiempo de expiración en milisegundos
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Construye un token JWT con los detalles especificados.
     *
     * @param extraClaims las reclamaciones adicionales
     * @param userDetails los detalles del usuario
     * @param expiration  el tiempo de expiración en milisegundos
     * @return el token JWT generado
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .subject(userDetails.getUsername()) // setSubject reemplazado por subject()
                .claims(extraClaims) // setClaims() reemplazado por claims()
                .issuedAt(new Date(System.currentTimeMillis())) // setIssuedAt() sigue igual
                .expiration(new Date(System.currentTimeMillis() + expiration)) // setExpiration() reemplazado por expiration()
                .signWith(getSignInKey()) // Se usa signWith con algoritmo recomendado
                .compact();
    }

    /**
     * Verifica si un token JWT es válido para un usuario específico.
     *
     * @param token       el token JWT
     * @param userDetails los detalles del usuario
     * @return true si el token es válido, false en caso contrario
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Verifica si un token JWT ha expirado.
     *
     * @param token el token JWT
     * @return true si el token ha expirado, false en caso contrario
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración de un token JWT.
     *
     * @param token el token JWT
     * @return la fecha de expiración
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae todas las reclamaciones de un token JWT.
     *
     * @param token el token JWT
     * @return las reclamaciones del token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey()) // Cambia de setSigningKey() a verifyWith()
                .build()
                .parseSignedClaims(token) // Cambia de parseClaimsJws() a parseSignedClaims()
                .getPayload(); // Cambia de getBody() a getPayload()
    }

    /**
     * Obtiene la clave secreta utilizada para firmar el JWT.
     *
     * @return la clave secreta como SecretKey
     */
    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
