package com.example.booksocial_backend.DTO.catalog;

/**
 * DTO de entrada para la creación y actualización de tomos.
 *
 * Contiene los datos necesarios para registrar un tomo dentro
 * de una edición concreta.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record CreateTomeRequest(

    Integer numberTome,

    Long editionId

) {
}