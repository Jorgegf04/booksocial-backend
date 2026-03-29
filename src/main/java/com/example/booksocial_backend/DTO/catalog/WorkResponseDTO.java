package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;
import java.util.List;

import com.example.booksocial_backend.domain.catalog.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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