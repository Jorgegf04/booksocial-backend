package com.example.booksocial_backend.domain.catalog;

import com.fasterxml.jackson.annotation.JsonCreator;

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