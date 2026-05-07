package com.example.booksocial_backend.exception;

public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(Long id) {
    super("Pedido no encontrado con id: " + id);
  }
  public OrderNotFoundException(String message) {
    super(message);
  }
}
