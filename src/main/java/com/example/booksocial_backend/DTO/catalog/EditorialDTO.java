package com.example.booksocial_backend.DTO.catalog;

/**
 * DTO de salida para la entidad Editorial.
 *
 * Representa una editorial dentro del catálogo sin incluir relaciones,
 * evitando exponer entidades del dominio y reduciendo el tamaño de las
 * respuestas.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record EditorialDTO(

    Long id,

    String name,

    String country

) {
}