package com.example.booksocial_backend.exception;

/**
 * Excepción lanzada cuando un usuario intenta realizar una acción
 * para la que no tiene permisos suficientes.
 */
public class UnauthorizedActionException extends RuntimeException {

  public UnauthorizedActionException(String message) {
    super(message);
  }
}
