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
   * Usuario que realiza el pedido. Puede ser null para pedidos de invitados.
   */
  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = true)
  private User user;

  /**
   * Email del comprador invitado (sin cuenta). Solo presente cuando user es null.
   */
  @Column(name = "guest_email", length = 100)
  private String guestEmail;

  /**
   * Líneas de pedido.
   */
  @Builder.Default
  @ToString.Exclude
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderLine> orderLines = new ArrayList<>();

}
