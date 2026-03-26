package com.example.booksocial_backend.exception;

/**
 * Excepción lanzada cuando un autor no es encontrado.
 */
public class AuthorNotFoundException extends RuntimeException {

  public AuthorNotFoundException(Long id) {
    super("Autor no encontrado con id: " + id);
  }
}