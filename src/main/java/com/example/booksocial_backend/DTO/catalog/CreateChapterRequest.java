package com.example.booksocial_backend.DTO.catalog;

/**
 * DTO de entrada para la creación y actualización de capítulos.
 *
 * Contiene únicamente los datos necesarios para persistir un capítulo
 * dentro de un tomo concreto.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record CreateChapterRequest(

                Integer chapterNumber,

                String title,

                Long tomeId

) {
}