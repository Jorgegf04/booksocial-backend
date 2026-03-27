package com.example.booksocial_backend.exception;

public class ChapterNotFoundException extends RuntimeException {

  public ChapterNotFoundException(Long id) {
    super("Capítulo no encontrado con id: " + id);
  }
}