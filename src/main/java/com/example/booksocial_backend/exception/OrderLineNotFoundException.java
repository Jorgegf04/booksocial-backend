package com.example.booksocial_backend.exception;

public class OrderLineNotFoundException extends RuntimeException {
  public OrderLineNotFoundException(Long id) {
    super("Línea de pedido no encontrada con id: " + id);
  }
}
