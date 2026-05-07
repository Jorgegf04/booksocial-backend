package com.example.booksocial_backend.exception;

public class EditionNotFoundException extends RuntimeException {
  public EditionNotFoundException(String message) {
    super(message);
  }
}
