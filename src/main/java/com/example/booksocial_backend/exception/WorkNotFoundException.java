package com.example.booksocial_backend.exception;

public class WorkNotFoundException extends RuntimeException {
  public WorkNotFoundException(String message) {
    super(message);
  }
}