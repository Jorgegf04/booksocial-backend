package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO de entrada para la creación y actualización de obras.
 *
 * Contiene los datos necesarios para registrar una obra dentro del sistema,
 * incluyendo los autores asociados mediante sus identificadores.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record CreateWorkRequest(

    String title,

    String description,

    String genre,

    LocalDate publicationDate,

    String img,

    Double averageRating,

    List<Long> authorIds

) {
}