package com.example.booksocial_backend.DTO.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la entidad Tome.
 *
 * Representa un tomo dentro de una edición sin incluir relaciones completas,
 * evitando exponer entidades del dominio y mejorando el rendimiento de la API.
 *
 * Incluye únicamente el identificador de la edición asociada.
 *
 * Este DTO es utilizado en las respuestas de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TomeResponseDTO {

    private Long id;
    private Integer numberTome;
    private Long editionId;
}