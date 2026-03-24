package com.example.booksocial_backend.DTO.commerce;

/**
 * DTO de salida para las líneas de pedido.
 *
 * Representa los productos incluidos en un pedido,
 * evitando exponer entidades del dominio.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record OrderLineDTO(

    Long productId,

    Integer quantity,

    Double unitaryPrice

) {
}