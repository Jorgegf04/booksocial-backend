package com.example.booksocial_backend.DTO.social;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de entrada para la creación de eventos.
 *
 * Permite registrar un evento exclusivo dentro de la plataforma.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record CreateEventRequest(

    String title,

    String description,

    LocalDateTime date,

    List<Long> userIds

) {
}