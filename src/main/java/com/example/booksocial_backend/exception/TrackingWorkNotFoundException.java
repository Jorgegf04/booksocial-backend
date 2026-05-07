package com.example.booksocial_backend.exception;

public class TrackingWorkNotFoundException extends RuntimeException {
  public TrackingWorkNotFoundException(Long id) {
    super("Seguimiento de obra no encontrado con id: " + id);
  }
  public TrackingWorkNotFoundException(Long userId, Long workId) {
    super("Seguimiento no encontrado para usuario=" + userId + " y obra=" + workId);
  }
}
