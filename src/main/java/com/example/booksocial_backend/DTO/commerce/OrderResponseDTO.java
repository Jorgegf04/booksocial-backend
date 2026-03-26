package com.example.booksocial_backend.DTO.commerce;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la entidad Order.
 *
 * Representa un pedido dentro del sistema de compra,
 * incluyendo sus líneas asociadas.
 *
 * Este DTO es utilizado en las respuestas de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private Long id;
    private LocalDateTime date;
    private Double total;
    private Long userId;
    private List<OrderLineResponseDTO> orderLines;
}