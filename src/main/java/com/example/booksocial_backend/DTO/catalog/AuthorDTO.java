package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {

        private Long id;
        private String name;
        private LocalDate birthDate;
        private List<String> works;
}