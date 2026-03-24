package com.example.booksocial_backend.DTO.user;

import com.example.booksocial_backend.domain.user.Role;

/**
 * DTO de entrada para crear usuario.
 */
public record CreateUserRequest(

    String username,

    String password,

    String email,

    String name,

    String secondName,

    Role role

) {
}