package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO de salida para la entidad Work.
 *
 * Representa una obra dentro del catálogo sin incluir relaciones completas,
 * evitando exponer entidades del dominio y mejorando el rendimiento de la API.
 *
 * Incluye únicamente los identificadores de los autores asociados.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record WorkDTO(

    Long id,

    String title,

    String description,

    String genre,

    LocalDate publicationDate,

    String img,

    Double averageRating,

    List<Long> authorIds

) {
}