package com.example.booksocial_backend.exception;

public class VolumeNotFoundException extends RuntimeException {
  public VolumeNotFoundException(String message) {
    super(message);
  }
}
