package com.example.booksocial_backend.exception;

/**
 * Excepción lanzada cuando un autor no es encontrado.
 */

public class AuthorNotFoundException extends RuntimeException {

  public AuthorNotFoundException(String message) {
    super(message);
  }
}