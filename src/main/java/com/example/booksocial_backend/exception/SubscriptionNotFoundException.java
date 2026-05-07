package com.example.booksocial_backend.exception;

public class SubscriptionNotFoundException extends RuntimeException {
  public SubscriptionNotFoundException(Long userId) {
    super("Suscripción no encontrada para el usuario con id: " + userId);
  }
}
