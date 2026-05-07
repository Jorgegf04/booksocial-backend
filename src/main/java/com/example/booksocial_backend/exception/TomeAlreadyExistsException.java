package com.example.booksocial_backend.exception;

public class TomeAlreadyExistsException extends RuntimeException {
  public TomeAlreadyExistsException(Integer number, Long editionId) {
    super("Ya existe el tomo número " + number + " en la edición con id=" + editionId);
  }
}
