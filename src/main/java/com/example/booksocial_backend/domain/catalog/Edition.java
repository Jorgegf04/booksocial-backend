package com.example.booksocial_backend.domain.catalog;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entidad que representa una edición concreta de una obra.
 *
 * Una misma obra puede tener múltiples ediciones publicadas por
 * diferentes editoriales o en distintos formatos.
 *
 * La edición es la entidad que conecta el catálogo con el sistema
 * de productos y ventas.
 *
 * @author Jorge
 * @since 12/03/2026
 */

@Entity
@Table(name = "EDITION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Edition {

  /**
   * Identificador único de la edición.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * ISBN de la edición.
   */
  @NotBlank
  @Size(max = 20)
  @Column(unique = true, length = 20, nullable = false)
  private String isbn;

  /**
   * Fecha de publicación de la edición.
   */
  @PastOrPresent
  @Column(name = "edition_date")
  private LocalDate editionDate;

  private String title;

  private Integer totalTomes;

  @Builder.Default
  @OneToMany(mappedBy = "edition", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Tome> tomes = new ArrayList<>();
  /**
   * Obra a la que pertenece esta edición.
   */

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "work_id", nullable = false)
  private Work work;

  /**
   * Editorial responsable de la edición.
   */
  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "editorial_id", nullable = false)
  private Editorial editorial;

  /**
   * Productos asociados a esta edición.
   */
  @Builder.Default
  @ToString.Exclude
  @JsonIgnore
  @OneToMany(mappedBy = "edition", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Product> products = new ArrayList<>();
}