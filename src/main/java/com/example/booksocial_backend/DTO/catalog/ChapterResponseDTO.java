package com.example.booksocial_backend.DTO.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la entidad Chapter.
 *
 * Representa la información básica de un capítulo sin incluir relaciones
 * completas.
 *
 * Se utiliza en respuestas de la API REST para evitar exponer entidades
 * del dominio.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterResponseDTO {

    private Long id;
    private Integer chapterNumber;
    private String title;
    private Long tomeId;
}