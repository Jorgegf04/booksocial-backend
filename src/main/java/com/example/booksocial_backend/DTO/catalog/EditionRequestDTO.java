package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para la creación y actualización de ediciones.
 *
 * Contiene los datos necesarios para registrar o modificar una edición,
 * incluyendo las referencias a la obra y la editorial.
 *
 * Este DTO es utilizado en las peticiones de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditionRequestDTO {

    @NotBlank(message = "El ISBN es obligatorio")
    @Size(max = 20)
    private String isbn;

    private LocalDate editionDate;

    @NotNull(message = "La edición debe estar asociada a una obra")
    private Long workId;

    @NotNull(message = "La edición debe estar asociada a una editorial")
    private Long editorialId;

    @Size(max = 255)
    private String title;

    private Integer totalTomes;
}