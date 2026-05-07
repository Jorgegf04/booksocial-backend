package com.example.booksocial_backend.domain.social;

import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un comentario publicado por un usuario
 * sobre una obra dentro del sistema.
 *
 * @author Jorge
 * @since 16/03/2026
 * @Version 1.0
 */

@Entity
@Table(name = "COMMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

  /**
   * Identificador del comentario.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Contenido de texto del comentario
   */
  @NotBlank
  @Size(max = 1000)
  @Column(nullable = false, length = 1000)
  private String content;

  /**
   * Fecha del comentario.
   */
  @NotNull
  @PastOrPresent
  @Column(nullable = false)
  private LocalDateTime date;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(nullable = false)
  private Boolean edited = false;

  /**
   * Relacion con usuario
   */
  @ToString.Exclude
  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private User user;

  /**
   * Relacion con obra
   */
  @ToString.Exclude
  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Work work;

  /**
   * Relacion con comentario padre
   */
  @JsonIgnore
  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Comment parent;

  /**
   * Relacion con las respuestas
   */
  @Builder.Default
  @ToString.Exclude
  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Comment> replies = new ArrayList<>();

  /**
   * Reacciones de un comentario
   */
  @Builder.Default
  @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  private List<Reaction> reactions = new ArrayList<>();

}
