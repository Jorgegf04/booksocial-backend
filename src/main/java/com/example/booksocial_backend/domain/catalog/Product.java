package com.example.booksocial_backend.domain.catalog;

import java.util.List;

import com.example.booksocial_backend.domain.commerce.OrderLine;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * Entidad que representa un producto comercializable dentro
 * del sistema de compra de BookSocial.
 *
 * Un producto está asociado a una edición concreta y contiene
 * la información necesaria para su venta como precio y stock.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 1.0
 */

@Entity
@Table(name = "PRODUCT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

  /**
   * Identificador único del producto.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Precio actual del producto.
   */
  @NotNull
  @Positive
  @Column(nullable = false)
  private Double price;

  /**
   * Cantidad disponible en stock.
   */
  @NotNull
  @Min(0)
  @Column(nullable = false)
  private Integer stock;

  /**
   * Edición a la que pertenece el producto.
   */
  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "edition_id", nullable = false)
  private Edition edition;

  @OneToMany(mappedBy = "product", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  private List<OrderLine> orderLines;
}