package com.example.booksocial_backend.domain.user;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa la relación de seguimiento entre usuarios.
 *
 * Un usuario puede seguir a otro usuario.
 */
@Entity
@Table(name = "USER_FOLLOW", uniqueConstraints = @UniqueConstraint(columnNames = { "follower_id", "following_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFollow {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Usuario que sigue
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "follower_id", nullable = false)
  private User follower;

  /**
   * Usuario seguido
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "following_id", nullable = false)
  private User following;

  /**
   * Fecha del seguimiento
   */
  private LocalDateTime followDate;
}