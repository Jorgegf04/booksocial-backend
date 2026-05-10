package com.example.booksocial_backend.domain.catalog;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidad que representa una editorial dentro del catálogo de BookSocial.
 *
 * Una editorial es la empresa responsable de publicar una o varias ediciones de
 * obras.
 * La relación con {@link Edition} es de uno a muchos — una editorial puede
 * tener
 * múltiples ediciones publicadas.
 * 
 * @author Jorge
 * @since 12/03/2026
 * @version 1.0
 */

@Entity
@Table(name = "EDITORIAL")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Editorial {

  /**
   * Identificador único de la editorial.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Nombre comercial de la editorial.
   */
  @NotBlank
  @Size(max = 150)
  @Column(nullable = false, unique = true, length = 150)
  private String name;

  /**
   * País de origen de la editorial.
   */
  @Size(max = 100)
  @Column(length = 100)
  private String country;

  /**
   * Lista de ediciones publicadas por esta editorial.
   * La clave foránea reside en la tabla de ediciones.
   */
  @Builder.Default
  @ToString.Exclude
  @OneToMany(mappedBy = "editorial", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  private List<Edition> editions = new ArrayList<>();

}
