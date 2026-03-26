package com.example.booksocial_backend.DTO.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para la creación y actualización de capítulos.
 *
 * Contiene únicamente los datos necesarios para persistir un capítulo
 * dentro de un tomo concreto.
 *
 * Este DTO es utilizado en las peticiones de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterRequestDTO {

  private Integer chapterNumber;
  private String title;
  private Long tomeId;
}