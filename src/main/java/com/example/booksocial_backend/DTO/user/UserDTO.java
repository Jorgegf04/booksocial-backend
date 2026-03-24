package com.example.booksocial_backend.DTO.user;

import java.time.LocalDate;

import com.example.booksocial_backend.domain.user.Role;

/**
 * DTO de salida para User.
 */
public record UserDTO(

    Long id,

    String username,

    String email,

    String name,

    String secondName,

    LocalDate registrationDate,

    Boolean active,

    Role role

) {
}