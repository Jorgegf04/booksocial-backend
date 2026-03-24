package com.example.booksocial_backend.DTO.social;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de salida para comentarios.
 *
 * Representa un comentario con sus respuestas directas,
 * evitando recursividad infinita.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record CommentDTO(

    Long id,

    String content,

    LocalDateTime date,

    Long userId,

    Long workId,

    Long parentId,

    List<CommentDTO> replies

) {
}