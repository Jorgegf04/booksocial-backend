package com.example.booksocial_backend.domain.commerce;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.booksocial_backend.domain.catalog.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

/**
 * Entidad que representa una línea dentro de un pedido.
 *
 * Cada línea corresponde a un producto concreto
 * y la cantidad adquirida.
 *
 * @author Jorge
 * @since 12/03/2026
 */

@Entity
@Table(name = "ORDER_LINE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLine {

  /**
   * Identificador de la línea de pedido.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Cantidad comprada.
   */
  @NotNull
  @Positive
  private Integer quantity;

  /**
   * Precio unitario del producto en el momento de la compra.
   */
  @NotNull
  @PositiveOrZero
  private Double unitaryPrice;

  /**
   * Pedido al que pertenece esta línea.
   */
  @ManyToOne
  @NotNull
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  /**
   * Producto comprado.
   */
  @ManyToOne
  @NotNull
  @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_orderline_product"))
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Product product;
}
