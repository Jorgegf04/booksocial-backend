package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;
import java.util.List;

public record AuthorDTO(
        Long id,
        String name,
        LocalDate birthDate,
        List<String> works) {
}