package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la entidad Author.
 *
 * Representa la información que se devuelve al cliente sobre un autor,
 * incluyendo sus datos básicos y las obras asociadas.
 *
 * Se utiliza para evitar exponer directamente la entidad del dominio.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDTO {

        private Long id;
        private String name;
        private String nationality;
        private LocalDate birthDate;

        private List<WorkResponseDTO> works;
}