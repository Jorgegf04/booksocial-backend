package com.example.booksocial_backend.exception;

public class EditionAlreadyExistsException extends RuntimeException {
  public EditionAlreadyExistsException(String isbn) {
    super("Ya existe una edición con el ISBN: " + isbn);
  }
}
