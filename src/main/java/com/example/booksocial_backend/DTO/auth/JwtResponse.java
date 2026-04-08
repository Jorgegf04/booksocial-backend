package com.example.booksocial_backend.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO de respuesta tras autenticación exitosa.
 *
 * Contiene el token JWT generado que el cliente
 * deberá enviar en futuras peticiones.
 *
 * @author Jorge
 * @since 2026
 */
@Data
@AllArgsConstructor
public class JwtResponse {

  private String token;
  private String type;
  private Long userId;
  private String username;
  private String role;
}