package com.example.booksocial_backend.DTO.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para la creación y actualización de tomos.
 *
 * Contiene los datos necesarios para registrar o modificar un tomo
 * dentro de una edición concreta.
 *
 * Este DTO es utilizado en las peticiones de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TomeRequestDTO {

    private Integer numberTome;
    private Long editionId;
}