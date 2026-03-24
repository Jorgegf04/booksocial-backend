package com.example.booksocial_backend.DTO.auth;

import lombok.Data;

/**
 * DTO utilizado para el registro de nuevos usuarios.
 *
 * Contiene los datos básicos necesarios para crear
 * una nueva cuenta en el sistema.
 *
 * @author Jorge
 * @since 2026
 */
@Data
public class RegisterRequest {

  private String username;
  private String email;
  private String password;
}