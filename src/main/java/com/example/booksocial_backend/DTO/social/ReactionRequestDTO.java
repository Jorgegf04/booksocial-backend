package com.example.booksocial_backend.DTO.social;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * DTO de entrada para realizar o eliminar una reacción (like)
 * sobre un comentario.
 *
 * Permite identificar el comentario sobre el cual el usuario
 * quiere añadir o quitar su reacción.
 *
 * Este DTO es utilizado en las peticiones de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionRequestDTO {

    @NotNull
    private Long commentId;

    private Long userId;

}