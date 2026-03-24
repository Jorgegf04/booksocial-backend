package com.example.booksocial_backend.DTO.social;

/**
 * DTO de entrada para crear comentarios o respuestas.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record CreateCommentRequest(

    String content,

    Long userId,

    Long workId,

    Long parentId

) {
}