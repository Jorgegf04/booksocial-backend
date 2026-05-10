package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para la creación y actualización de autores.
 *
 * Contiene los datos necesarios para registrar o modificar un autor
 * dentro del sistema.
 *
 * Este DTO es utilizado en las peticiones de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorRequestDTO {

    @NotBlank(message = "El nombre del autor es obligatorio")
    @Size(max = 150)
    private String name;

    @Size(max = 100)
    private String nationality;

    private LocalDate birthDate;

    @Size(max = 500)
    private String img;

    private List<Long> workIds;
}