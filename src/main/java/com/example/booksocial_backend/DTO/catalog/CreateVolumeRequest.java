package com.example.booksocial_backend.DTO.catalog;

/**
 * DTO de entrada para la creación y actualización de volúmenes.
 *
 * Contiene los datos necesarios para registrar un volumen dentro
 * de una edición concreta.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record CreateVolumeRequest(

    Integer volumeNumber,

    String title,

    Long editionId

) {
}