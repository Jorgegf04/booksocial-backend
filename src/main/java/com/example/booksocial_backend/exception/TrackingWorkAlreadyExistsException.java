package com.example.booksocial_backend.exception;

public class TrackingWorkAlreadyExistsException extends RuntimeException {
  public TrackingWorkAlreadyExistsException(Long userId, Long workId) {
    super("El usuario con id=" + userId + " ya sigue la obra con id=" + workId);
  }
}
