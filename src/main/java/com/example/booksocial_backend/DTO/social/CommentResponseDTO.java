package com.example.booksocial_backend.DTO.social;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para comentarios.
 *
 * Representa un comentario con sus respuestas directas,
 * evitando recursividad infinita mediante una estructura controlada.
 *
 * Incluye información del usuario, la obra asociada y
 * la jerarquía de comentarios.
 *
 * Este DTO es utilizado en las respuestas de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {

    private Long id;
    private String content;
    private LocalDateTime date;
    private LocalDateTime updatedAt;
    private Boolean edited;
    private Long userId;
    private String username;

    private Long workId;
    private String workTitle;

    private Long parentId;

    private List<CommentResponseDTO> replies;
}