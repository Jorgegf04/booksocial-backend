package com.example.booksocial_backend.DTO.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la entidad Editorial.
 *
 * Representa una editorial dentro del catálogo sin incluir relaciones,
 * evitando exponer entidades del dominio y reduciendo el tamaño de las
 * respuestas.
 *
 * Este DTO es utilizado en las respuestas de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditorialResponseDTO {

    private Long id;
    private String name;
    private String country;
    private Integer totalEditions;
}