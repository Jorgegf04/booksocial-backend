package com.example.booksocial_backend.DTO.social;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la entidad Event.
 *
 * Representa un evento exclusivo dentro de la plataforma,
 * incluyendo los usuarios participantes mediante sus identificadores.
 *
 * Este DTO es utilizado en las respuestas de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime date;
    private List<Long> userIds;
}