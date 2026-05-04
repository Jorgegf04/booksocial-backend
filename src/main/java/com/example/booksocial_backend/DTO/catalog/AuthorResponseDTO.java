package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDTO {

    private Long id;
    private String name;
    private String nationality;
    private LocalDate birthDate;
    private String img;
    private Long followerCount;
    private List<WorkResponseDTO> works;
}
