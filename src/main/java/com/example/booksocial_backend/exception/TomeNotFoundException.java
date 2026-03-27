package com.example.booksocial_backend.exception;

public class TomeNotFoundException extends RuntimeException {

  public TomeNotFoundException(Long id) {
    super("Tomo no encontrado con id: " + id);
  }
}