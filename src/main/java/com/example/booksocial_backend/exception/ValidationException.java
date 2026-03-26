package com.example.booksocial_backend.exception;

public class ValidationException extends RuntimeException {

  public ValidationException(String message) {
    super(message);
  }
}