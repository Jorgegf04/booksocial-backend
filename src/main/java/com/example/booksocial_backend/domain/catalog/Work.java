package com.example.booksocial_backend.domain.catalog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidad que representa una obra dentro del catálogo del sistema BookSocial.
 *
 * Una obra es el concepto abstracto que agrupa la información general de un
 * libro, manga o cómic. A partir de esta entidad se gestionan las distintas
 * ediciones publicadas por diferentes editoriales.
 *
 * Relación principal:
 * Work -> Edition (1:N)
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 1.0
 */
@Entity
@Table(name = "WORK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Work {
  /**
   * Identificador único de la obra.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Nombre de la obra
   */
  @NotBlank
  @Size(max = 255)
  @Column(nullable = false, length = 255)
  private String title;

  /**
   * Título de la obra.
   */
  @Size(max = 2000)
  @Column(length = 2000)
  private String description;

  /**
   * Género literario principal.
   */
  @NotBlank
  @Size(max = 100)
  @Column(length = 100)
  private String genre;

  /**
   * Fecha de publicación original de la obra.
   */
  @PastOrPresent
  @Column(name = "publication_date")
  private LocalDate publicationDate;

  /**
   * Imagen representativa de la obra.
   */
  @Size(max = 500)
  private String img;

  /**
   * Valoración media calculada a partir de las opiniones de los usuarios.
   */
  @Min(0)
  @Max(5)
  private Double averageRating;

  /**
   * Autores asociados a la obra.
   * Relación muchos a muchos.
   */
  @ToString.Exclude
  @ManyToMany
  @JoinTable(name = "WORK_AUTHOR", joinColumns = @JoinColumn(name = "work_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
  private List<Author> authors = new ArrayList<>();

  /**
   * Ediciones publicadas de esta obra.
   */
  @ToString.Exclude
  @OneToMany(mappedBy = "work", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Edition> editions = new ArrayList<>();

}
