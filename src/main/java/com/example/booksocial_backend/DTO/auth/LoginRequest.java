package com.example.booksocial_backend.DTO.auth;

import lombok.Data;

/**
 * DTO utilizado para la autenticación de usuarios.
 *
 * Contiene las credenciales necesarias para iniciar sesión
 * en el sistema (username y password).
 *
 * @author Jorge
 * @since 2026
 */
@Data
public class LoginRequest {

  private String username;
  private String password;
}