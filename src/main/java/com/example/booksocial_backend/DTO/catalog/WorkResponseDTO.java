package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la entidad Work.
 *
 * Representa una obra dentro del catálogo sin incluir relaciones completas,
 * evitando exponer entidades del dominio y mejorando el rendimiento de la API.
 *
 * Incluye únicamente los identificadores de los autores asociados.
 *
 * Este DTO es utilizado en las respuestas de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String genre;
    private LocalDate publicationDate;
    private String img;
    private Double averageRating;
    private List<Long> authorIds;
}