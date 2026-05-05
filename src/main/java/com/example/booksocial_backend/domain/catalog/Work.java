package com.example.booksocial_backend.domain.catalog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.booksocial_backend.domain.social.TrackingWork;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import jakarta.validation.constraints.NotNull;
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
   * Género literario principal.
   */
  @NotNull
  @Enumerated(EnumType.STRING)
  private Genre genre;

  @NotNull
  @Enumerated(EnumType.STRING)
  private WorkType type;

  // opcional
  @Enumerated(EnumType.STRING)
  private Demographic demographic;

  /**
   * Autores asociados a la obra.
   * Relación muchos a muchos.
   */
  @Builder.Default
  @ToString.Exclude
  @ManyToMany
  @JoinTable(name = "WORK_AUTHOR", joinColumns = @JoinColumn(name = "work_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
  private List<Author> authors = new ArrayList<>();

  /**
   * Ediciones publicadas de esta obra.
   */
  @Builder.Default
  @ToString.Exclude
  @OneToMany(mappedBy = "work", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Edition> editions = new ArrayList<>();

  /**
   * Lista de seguimientos asociados a esta obra.
   *
   * <p>
   * Representa todos los usuarios que siguen esta obra dentro del sistema,
   * junto con su estado de seguimiento (pendiente, leyendo, completada, etc).
   * </p>
   *
   * <p>
   * Relación:
   * </p>
   * <ul>
   * <li>Work → TrackingWork (1:N)</li>
   * </ul>
   *
   * <p>
   * Se utiliza carga perezosa (LAZY) para optimizar el rendimiento
   * y evitar cargar innecesariamente los seguimientos.
   * </p>
   */
  @Builder.Default
  @JsonIgnore
  @ToString.Exclude
  @OneToMany(mappedBy = "work", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<TrackingWork> trackingWorks = new ArrayList<>();
}
