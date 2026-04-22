package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;
import java.util.List;

import com.example.booksocial_backend.domain.catalog.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta que representa una obra del catálogo.
 *
 * <p>Contiene la información pública de la obra que se expone a través
 * de la API REST. Los autores se incluyen como lista de nombres en lugar
 * de entidades completas para evitar dependencias circulares en la serialización.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkResponseDTO {

    private Long id;
    private String title;
    private String description;
    private Genre genre;
    private WorkType type;
    private Demographic demographic;
    private LocalDate publicationDate;
    private String img;
    private Double averageRating;

    private List<String> authors;
}