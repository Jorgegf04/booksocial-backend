package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la entidad Edition.
 *
 * Representa una edición de una obra sin incluir relaciones completas,
 * evitando exponer entidades del dominio y reduciendo el tamaño de las
 * respuestas.
 *
 * Incluye únicamente los identificadores de las entidades relacionadas.
 *
 * Este DTO es utilizado en las respuestas de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditionResponseDTO {

    private Long id;
    private String isbn;
    private LocalDate editionDate;

    private Long workId;
    private String workTitle;

    private Long editorialId;
    private String editorialName;

    private Integer totalTomes;
}