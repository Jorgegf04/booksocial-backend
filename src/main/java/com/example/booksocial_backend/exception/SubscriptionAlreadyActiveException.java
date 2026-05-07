package com.example.booksocial_backend.exception;

public class SubscriptionAlreadyActiveException extends RuntimeException {
  public SubscriptionAlreadyActiveException(Long userId) {
    super("El usuario con id=" + userId + " ya tiene una suscripción activa");
  }
}
