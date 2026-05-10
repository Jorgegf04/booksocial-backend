package com.example.booksocial_backend.domain.user;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidad que representa la suscripción mensual de un usuario dentro del
 * sistema BookSocial.
 *
 * La suscripción permite a los usuarios acceder a funcionalidades adicionales,
 * como descuentos en compras, participación en eventos exclusivos y acceso
 * a estadísticas avanzadas dentro de la plataforma.
 *
 * Cada usuario puede tener como máximo una única suscripción activa en un
 * momento dado,
 * lo que se garantiza mediante una relación uno a uno con la entidad
 * {@link User}.
 *
 * La suscripción se activa en una fecha determinada y se renueva
 * automáticamente
 * cada 30 días, calculando la fecha de finalización a partir de la fecha de
 * inicio.
 *
 * Esta entidad depende directamente del usuario, por lo que su existencia está
 * ligada a la de la entidad {@link User}.
 *
 * @author Jorge
 * @since 23/03/2026
 * @version 1.0
 */

@Entity
@Table(name = "SUBSCRIPTION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription {

  /**
   * Identificador único de la suscripción.
   */

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Fecha en la que la suscripción fue activada por primera vez.
   */
  @NotNull
  @PastOrPresent
  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  /**
   * Fecha en la que la suscripción finaliza o se renovará.
   * Se calcula como startDate + 30 días automáticamente.
   */
  @NotNull
  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  /**
   * Indica si la suscripción se encuentra activa en este momento.
   */
  @NotNull
  @Builder.Default
  @Column(nullable = false)
  private Boolean activated = true;

  /**
   * Usuario propietario de esta suscripción.
   * Relación uno a uno — la clave foránea reside en esta tabla.
   */
  @NotNull
  @ToString.Exclude
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

}
