package com.example.booksocial_backend.exception;

public class ChapterAlreadyExistsException extends RuntimeException {

  public ChapterAlreadyExistsException(Integer number, Long tomeId) {
    super("Ya existe un capítulo con número " + number + " en el tomo " + tomeId);
  }
}