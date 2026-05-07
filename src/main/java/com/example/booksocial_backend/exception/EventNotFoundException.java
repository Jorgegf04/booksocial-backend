package com.example.booksocial_backend.exception;

public class EventNotFoundException extends RuntimeException {
  public EventNotFoundException(Long id) {
    super("Evento no encontrado con id: " + id);
  }
}
