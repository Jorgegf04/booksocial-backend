package com.example.booksocial_backend.DTO.catalog;

/**
 * DTO de entrada para la creación y actualización de productos.
 *
 * Contiene los datos necesarios para registrar un producto
 * dentro del sistema de compra.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record CreateProductRequest(

    Double price,

    Integer stock,

    Long editionId

) {
}