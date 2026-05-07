package com.example.booksocial_backend.exception;

public class CommentNotFoundException extends RuntimeException {
  public CommentNotFoundException(Long id) {
    super("Comentario no encontrado con id: " + id);
  }
}
