package com.example.booksocial_backend.DTO.user;

/**
 * DTO de actualización de usuario.
 */
public record UpdateUserRequest(

    String username,

    String email,

    String name,

    String secondName

) {
}
