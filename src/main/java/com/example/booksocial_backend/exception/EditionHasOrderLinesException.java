package com.example.booksocial_backend.exception;

public class EditionHasOrderLinesException extends RuntimeException {
  public EditionHasOrderLinesException(Long editionId) {
    super("No se puede eliminar la edición con id=" + editionId + " porque tiene pedidos históricos asociados. Elimine primero los pedidos o desasocie los productos.");
  }
}
