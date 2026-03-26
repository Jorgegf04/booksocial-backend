package com.example.booksocial_backend.DTO.commerce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa una línea de pedido.
 *
 * Contiene la información de los productos incluidos en un pedido,
 * como el identificador del producto, la cantidad solicitada y el precio
 * unitario.
 *
 * Se utiliza tanto en peticiones como en respuestas de la API REST.
 *
 * Evita exponer directamente las entidades del dominio.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineResponseDTO {

    private Long productId;
    private Integer quantity;
    private Double unitaryPrice;
}