package com.example.booksocial_backend.DTO.social;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * DTO de entrada para la creación de comentarios o respuestas.
 *
 * Contiene la información necesaria para registrar un comentario,
 * incluyendo el usuario, la obra asociada y opcionalmente el comentario padre.
 *
 * Este DTO es utilizado en las peticiones de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {

    @NotBlank
    private String content;

    @NotNull
    private Long userId;

    @NotNull
    private Long workId;

    private Long parentId; // null si es comentario raíz
}