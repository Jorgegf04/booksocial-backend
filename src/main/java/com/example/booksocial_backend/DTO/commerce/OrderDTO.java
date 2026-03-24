package com.example.booksocial_backend.DTO.commerce;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de salida para la entidad Order.
 *
 * Representa un pedido dentro del sistema de compra,
 * incluyendo sus líneas asociadas.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record OrderDTO(

    Long id,

    LocalDateTime date,

    Double total,

    Long userId,

    List<OrderLineDTO> orderLines

) {
}
