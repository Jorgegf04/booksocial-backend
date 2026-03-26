package com.example.booksocial_backend.exception;

/**
 * Excepción lanzada cuando se intenta crear un autor duplicado.
 */
public class AuthorAlreadyExistsException extends RuntimeException {

  public AuthorAlreadyExistsException(String name) {
    super("Ya existe un autor con el nombre: " + name);
  }
}