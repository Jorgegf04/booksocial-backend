package com.example.booksocial_backend.exception;

public class EditorialAlreadyExistsException extends RuntimeException {
  public EditorialAlreadyExistsException(String name) {
    super("Ya existe una editorial con el nombre: " + name);
  }
}
