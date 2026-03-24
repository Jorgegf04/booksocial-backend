package com.example.booksocial_backend.exception;

/**
 * Excepción lanzada cuando ya existe un usuario.
 */
public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}