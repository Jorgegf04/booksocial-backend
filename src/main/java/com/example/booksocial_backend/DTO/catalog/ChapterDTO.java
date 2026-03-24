package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;

/**
 * DTO de salida para la entidad Chapter.
 *
 * Representa la información básica de un capítulo sin incluir relaciones
 * completas.
 * Se utiliza en respuestas de la API REST para evitar exponer entidades del
 * dominio.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record ChapterDTO(

    Long id,

    Integer chapterNumber,

    String title,

    Long tomeId

) {
}