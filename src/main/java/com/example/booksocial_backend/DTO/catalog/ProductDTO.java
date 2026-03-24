package com.example.booksocial_backend.DTO.catalog;

/**
 * DTO de salida para la entidad Product.
 *
 * Representa un producto comercializable dentro del sistema,
 * incluyendo información de precio, stock y la edición asociada
 * mediante su identificador.
 *
 * Evita exponer directamente la entidad Edition.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record ProductDTO(

    Long id,

    Double price,

    Integer stock,

    Long editionId

) {
}