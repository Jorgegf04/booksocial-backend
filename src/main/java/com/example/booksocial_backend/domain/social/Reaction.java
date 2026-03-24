package com.example.booksocial_backend.domain.social;

import com.example.booksocial_backend.domain.user.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una reacción (like) realizada por un usuario
 * sobre un comentario dentro del sistema BookSocial.
 *
 * Cada reacción está asociada a un único usuario y a un único comentario.
 * El sistema garantiza que un usuario solo puede realizar una única
 * reacción por comentario mediante una restricción de unicidad en la base de
 * datos.
 *
 * Esta entidad forma parte del sistema social de la plataforma, permitiendo
 * a los usuarios interactuar con los comentarios publicados por otros usuarios.
 *
 * @author Jorge
 * @since 12/03/2026
 * @version 1.0
 */

@Entity
@Table(name = "REACTION", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "user_id", "comment_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reaction {

  /**
   * Identificador único de la reacción.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Fecha en la que se realiza la reacción.
   *
   * Este valor se asigna automáticamente en el momento en que el usuario
   * interactúa con el comentario y debe ser una fecha pasada o actual.
   */
  @NotNull
  @PastOrPresent
  private LocalDateTime date;

  /**
   * Usuario que realiza la reacción.
   *
   * Esta relación es obligatoria y permite identificar al usuario que ha
   * dado "like" al comentario.
   */
  @NotNull
  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private User user;

  /**
   * Comentario sobre el que se realiza la reacción.
   *
   * Esta relación es obligatoria y permite asociar la reacción al comentario
   * correspondiente dentro del sistema.
   */
  @NotNull
  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Comment comment;

}
