package com.example.booksocial_backend.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Clase utilitaria encargada de la gestión de tokens JWT.
 *
 * Proporciona funcionalidades para:
 * - Generar tokens de autenticación.
 * - Extraer información del token.
 * - Validar la integridad y validez del token.
 *
 * El token contiene información del usuario autenticado y
 * se utiliza para autenticar peticiones posteriores sin necesidad de sesión.
 *
 * @author Jorge
 * @since 2026
 * @version 1.0
 */
@Component
public class JwtUtils {

  private final String jwtSecret = "mySecretKeymySecretKeymySecretKeymySecretKey";
  private final int jwtExpirationMs = 86400000;

  private Key key() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  /**
   * Genera un token JWT a partir del username.
   *
   * @param username nombre del usuario autenticado
   * @return token JWT generado
   */
  public String generateJwtToken(Authentication authentication) {

    UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

    return Jwts.builder()
            .setSubject(userPrincipal.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
}

  /**
   * Extrae el username del token JWT.
   *
   * @param token token JWT
   * @return username contenido en el token
   */
  public String getUsernameFromJwtToken(String token) {

    return Jwts.parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  /**
   * Valida la integridad y expiración del token JWT.
   *
   * @param token token JWT
   * @return true si es válido, false en caso contrario
   */
  public boolean validateJwtToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}