package com.example.booksocial_backend.exception;

/**
 * Excepción lanzada cuando un usuario no es encontrado.
 */
public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(Long id) {
    super("Usuario no encontrado con id: " + id);
  }
}
