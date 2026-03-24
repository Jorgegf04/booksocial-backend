package com.example.booksocial_backend.domain.commerce;

import com.example.booksocial_backend.domain.user.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un pedido realizado por un usuario.
 *
 * Un pedido puede contener múltiples líneas de pedido
 * que representan los productos comprados.
 *
 * @author Jorge
 * @since 15/03/2026
 * @version 1.0
 */

@Entity
@Table(name = "ORDERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

  /**
   * Identificador del pedido.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Fecha en la que se realiza el pedido.
   */
  @NotNull
  @PastOrPresent
  @Column(nullable = false)
  private LocalDateTime date;

  /**
   * Precio total del pedido.
   */
  @NotNull
  @PositiveOrZero
  @Column(nullable = false)
  private Double total;

  /**
   * Usuario que realiza el pedido.
   */
  @NotNull
  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  /**
   * Líneas de pedido.
   */
  @ToString.Exclude
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderLine> orderLines = new ArrayList<>();

}
