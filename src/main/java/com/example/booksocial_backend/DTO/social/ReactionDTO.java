package com.example.booksocial_backend.DTO.social;

import java.time.LocalDateTime;

/**
 * DTO de salida para la entidad Reaction.
 *
 * Representa una reacción (like) de un usuario sobre un comentario.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record ReactionDTO(

    Long id,

    LocalDateTime date,

    Long userId,

    Long commentId

) {
}