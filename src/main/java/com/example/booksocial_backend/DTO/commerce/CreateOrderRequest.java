package com.example.booksocial_backend.DTO.commerce;

import java.util.List;

/**
 * DTO de entrada para la creación de pedidos.
 *
 * Contiene la información necesaria para registrar
 * un pedido en el sistema.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record CreateOrderRequest(

    Long userId,

    List<OrderLineDTO> orderLines

) {
}