package com.example.booksocial_backend.domain.social;

import java.time.LocalDateTime;

import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.domain.user.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entidad que representa el seguimiento de una obra por parte de un usuario.
 *
 * <p>
 * Esta entidad forma parte del sistema social de BookSocial y permite a los
 * usuarios
 * seguir obras para recibir información, consultar contenido o marcar interés.
 * </p>
 *
 * <p>
 * Relaciones principales:
 * </p>
 * <ul>
 * <li>Usuario → TrackingWork (1:N)</li>
 * <li>Work → TrackingWork (1:N)</li>
 * </ul>
 *
 * @author Jorge
 * @since 2026
 * @version 1.0
 */
@Entity
@Table(name = "TRACKING_WORK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingWork {

  /**
   * Identificador único del seguimiento.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Estado del seguimiento de la obra.
   */
  @NotNull
  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TrackingWorkStatus status = TrackingWorkStatus.PENDING;

  /**
   * Usuario que realiza el seguimiento de la obra.
   */
  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  /**
   * Obra que está siendo seguida.
   */
  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "work_id", nullable = false)
  private Work work;

  /**
   * Fecha en la que el usuario comienza a seguir la obra.
   */
  @NotNull
  @Column(nullable = false)
  private LocalDateTime date;
}