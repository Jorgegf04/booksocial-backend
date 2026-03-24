package com.example.booksocial_backend.DTO.social;

/**
 * DTO de entrada para realizar una reacción sobre un comentario.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record ToggleReactionRequest(

    Long userId,

    Long commentId

) {
}