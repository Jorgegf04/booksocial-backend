package com.example.booksocial_backend.DTO.social;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de salida para la entidad Event.
 *
 * Representa un evento exclusivo dentro de la plataforma,
 * incluyendo los usuarios participantes mediante sus identificadores.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record EventDTO(

    Long id,

    String title,

    String description,

    LocalDateTime date,

    List<Long> userIds

) {
}