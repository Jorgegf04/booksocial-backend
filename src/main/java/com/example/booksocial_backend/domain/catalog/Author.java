package com.example.booksocial_backend.domain.catalog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidad que representa a un autor dentro del catálogo de BookSocial.
 *
 * Un autor puede estar relacionado con una o varias obras. La relación
 * entre autores y obras es de muchos a muchos, gestionada desde la entidad
 * {@link Work}.
 * 
 * @author Jorge
 * @since 12/03/2026
 * @version 1.0
 */

@Entity
@Table(name = "AUTHOR")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Author {

  /**
   * Identificador único del autor.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Nombre completo del autor tal como aparece en las publicaciones.
   */
  @NotBlank
  @Size(max = 150)
  @Column(nullable = false, length = 150)
  private String name;

  /**
   * Nacionalidad del autor
   * 
   */
  @Column(name = "nationality")
  private String nationality;

  /**
   * Fecha de nacimiento del autor.
   * Puede ser nula si no se dispone de la información.
   */
  @Past
  @Column(name = "birth_date")
  private LocalDate birthDate;

  /**
   * Lista de obras en las que este autor ha participado.
   * La relación es gestionada por la entidad {@link Work} (mappedBy).
   */
  @Builder.Default
  @ToString.Exclude
  @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
  private List<Work> works = new ArrayList<>();
}
