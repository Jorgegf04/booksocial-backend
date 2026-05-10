package com.example.booksocial_backend.DTO.catalog;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

  @NotNull(message = "El número de capítulo es obligatorio")
  @Positive
  private Integer chapterNumber;

  @Size(max = 200)
  private String title;

  @NotNull(message = "El capítulo debe estar asociado a un tomo")
  private Long tomeId;
}