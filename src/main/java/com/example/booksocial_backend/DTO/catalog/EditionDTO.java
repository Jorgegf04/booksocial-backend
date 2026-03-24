package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;

/**
 * DTO de salida para la entidad Edition.
 *
 * Representa una edición de una obra sin incluir relaciones completas,
 * evitando exponer entidades del dominio y reduciendo el tamaño de las
 * respuestas.
 *
 * Incluye únicamente los identificadores de las entidades relacionadas.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record EditionDTO(

    Long id,

    String isbn,

    LocalDate editionDate,

    Long workId,

    Long editorialId

) {
}