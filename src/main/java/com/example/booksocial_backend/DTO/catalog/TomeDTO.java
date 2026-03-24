package com.example.booksocial_backend.DTO.catalog;

/**
 * DTO de salida para la entidad Tome.
 *
 * Representa un tomo dentro de una edición sin incluir relaciones completas,
 * evitando exponer entidades del dominio y mejorando el rendimiento de la API.
 *
 * Incluye únicamente el identificador de la edición asociada.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record TomeDTO(

    Long id,

    Integer numberTome,

    Long editionId

) {
}