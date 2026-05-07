package com.example.booksocial_backend.exception;

public class ReactionNotFoundException extends RuntimeException {
  public ReactionNotFoundException(Long userId, Long commentId) {
    super("Reacción no encontrada para usuario=" + userId + " y comentario=" + commentId);
  }
}
