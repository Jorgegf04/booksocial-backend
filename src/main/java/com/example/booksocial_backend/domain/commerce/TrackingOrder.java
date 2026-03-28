package com.example.booksocial_backend.domain.commerce;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entidad que representa el seguimiento de un pedido.
 *
 * Permite registrar el estado logístico del pedido
 * durante el proceso de envío.
 *
 * @author Jorge
 * @since 15/03/2026
 * @version 1.0
 */

@Entity
@Table(name = "TRACKING_ORDER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingOrder {

  /**
   * Identificador del tracking.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Estado actual del envío.
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false)
  private TrackingOrderStatus status;

  /**
   * Fecha del cambio de estado.
   */
  @NotNull
  @PastOrPresent
  @Column(nullable = false)
  private LocalDateTime date;

  /**
   * Pedido asociado.
   */
  @NotNull
  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;
}
