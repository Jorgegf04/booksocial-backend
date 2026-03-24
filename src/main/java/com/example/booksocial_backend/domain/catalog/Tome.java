package com.example.booksocial_backend.domain.catalog;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un tomo dentro de una edición de manga.
 *
 * Un tomo agrupa varios capítulos de una obra.
 *
 * @author Jorge
 * @since 12/03/2026
 */

@Entity
@Table(name = "TOME")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tome {

  /**
   * Identificador único del tomo.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Número del tomo dentro de la colección.
   */
  @NotNull
  @Positive
  @Column(nullable = false)
  private Integer numberTome;

  /**
   * Edición a la que pertenece el tomo.
   */
  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "edition_id", nullable = false)
  private Edition edition;

  /**
   * Capítulos incluidos en este tomo.
   */
  @ToString.Exclude
  @OneToMany(mappedBy = "tome", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Chapter> chapters = new ArrayList<>();
}