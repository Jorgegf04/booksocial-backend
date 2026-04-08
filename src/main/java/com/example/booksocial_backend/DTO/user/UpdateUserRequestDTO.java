package com.example.booksocial_backend.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para actualizar un usuario.
 *
 * Permite modificar los datos básicos del perfil del usuario.
 *
 * Utilizado en peticiones PUT de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDTO {

    @Size(min = 3, max = 50)
    private String username;

    @Email
    private String email;

    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String secondName;

    @Size(max = 500)
    private String img;

}