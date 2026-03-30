package com.example.booksocial_backend.DTO.social;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la entidad Reaction.
 *
 * Representa una reacción (like) de un usuario sobre un comentario,
 * incluyendo la fecha en la que se realizó.
 *
 * Este DTO es utilizado en las respuestas de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionResponseDTO {

    private Long id;
    private LocalDateTime date;

    private Long userId;
    private String username;

    private Long commentId;

    private Boolean liked;
}