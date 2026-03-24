package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;

public record CreateAuthorRequest(
    String name,
    LocalDate birthDate) {
}