package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;

/**
 * DTO de entrada para la creación y actualización de ediciones.
 *
 * Contiene los datos necesarios para registrar una nueva edición,
 * incluyendo las referencias a la obra y la editorial.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record CreateEditionRequest(

    String isbn,

    LocalDate editionDate,

    Long workId,

    Long editorialId

) {
}