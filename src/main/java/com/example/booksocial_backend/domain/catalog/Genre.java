package com.example.booksocial_backend.domain.catalog;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Enumeración de géneros literarios disponibles en el catálogo de BookSocial.
 *
 * <p>Cubre géneros universales (FANTASY, ROMANCE, THRILLER…) y géneros
 * específicos del manga y el cómic (ISEKAI, MECHA, SUPERHERO…).
 * La anotación {@code @JsonCreator} permite deserializar el valor desde JSON
 * de forma insensible a mayúsculas.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
public enum Genre {

  FANTASY,
  SCIENCE_FICTION,
  ROMANCE,
  HORROR,
  THRILLER,
  DRAMA,
  COMEDY,
  ACTION,
  ADVENTURE,
  MYSTERY,
  HISTORICAL,

  PHILOSOPHICAL,
  PSYCHOLOGICAL,
  SLICE_OF_LIFE,
  TRAGEDY,
  CRIME,
  SUSPENSE,
  WAR,
  BIOGRAPHY,

  SUPERNATURAL,
  MECHA,
  ISEKAI,
  ECCHI,
  HAREM,
  MARTIAL_ARTS,
  SCHOOL,
  SPORTS,

  SUPERHERO,
  DARK_FANTASY,
  CYBERPUNK,
  POST_APOCALYPTIC;

  @JsonCreator
  public static Genre from(String value) {
    return Genre.valueOf(value.toUpperCase());
  }
}