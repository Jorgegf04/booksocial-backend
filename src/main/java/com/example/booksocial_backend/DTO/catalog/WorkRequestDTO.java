package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;
import java.util.List;

import com.example.booksocial_backend.domain.catalog.Demographic;
import com.example.booksocial_backend.domain.catalog.Genre;
import com.example.booksocial_backend.domain.catalog.WorkType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para la creación y actualización de obras.
 *
 * Contiene los datos necesarios para registrar o modificar una obra
 * dentro del sistema, incluyendo los autores asociados mediante
 * sus identificadores.
 *
 * Este DTO es utilizado en las peticiones de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkRequestDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255)
    private String title;

    @Size(max = 2000)
    private String description;

    @NotNull(message = "El género es obligatorio")
    private Genre genre;

    @NotNull(message = "El tipo de obra es obligatorio")
    private WorkType type;

    private Demographic demographic;
    private LocalDate publicationDate;

    @Size(max = 500)
    private String img;

    private Double averageRating;

    private List<String> authors;
}