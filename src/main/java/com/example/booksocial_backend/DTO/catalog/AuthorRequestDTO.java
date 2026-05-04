package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;
import java.util.List;

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

    private String name;
    private String nationality;
    private LocalDate birthDate;
    private String img;
    private List<Long> workIds;

}