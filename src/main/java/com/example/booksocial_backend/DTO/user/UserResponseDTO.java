package com.example.booksocial_backend.DTO.user;

import java.time.LocalDate;

import com.example.booksocial_backend.domain.user.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la entidad User.
 *
 * Representa la información de un usuario dentro del sistema,
 * incluyendo sus datos personales, estado y rol.
 *
 * Utilizado en las respuestas de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String name;
    private String secondName;
    private String img;
    private LocalDate registrationDate;
    private Boolean active;
    private Role role;

}