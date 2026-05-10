package com.example.booksocial_backend.exception;

/**
 * Excepción lanzada cuando el stock disponible de un producto
 * es insuficiente para completar la cantidad solicitada en el pedido.
 */
public class InsufficientStockException extends RuntimeException {

  public InsufficientStockException(Long productId, int requested, int available) {
    super("Stock insuficiente para el producto con id " + productId
        + ": solicitado=" + requested + ", disponible=" + available);
  }

  public InsufficientStockException(String message) {
    super(message);
  }
}
