package com.example.booksocial_backend.exception;

public class TrackingOrderNotFoundException extends RuntimeException {
  public TrackingOrderNotFoundException(Long id) {
    super("Seguimiento de pedido no encontrado con id: " + id);
  }
}
