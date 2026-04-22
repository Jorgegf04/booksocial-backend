package com.example.booksocial_backend.DTO.commerce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para crear una línea de pedido.
 *
 * <p>Cada línea de pedido referencia un {@code Product} concreto y especifica
 * la cantidad deseada. El precio unitario no se incluye aquí; se toma del
 * producto en el momento de la creación para garantizar coherencia histórica.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineRequestDTO {

  private Long productId;
  private Long orderId;
  private Integer quantity;

}