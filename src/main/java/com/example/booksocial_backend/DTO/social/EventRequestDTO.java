package com.example.booksocial_backend.DTO.social;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * DTO de entrada para la creación y actualización de eventos.
 *
 * Permite registrar o modificar un evento exclusivo dentro de la plataforma,
 * incluyendo los usuarios participantes mediante sus identificadores.
 *
 * Este DTO es utilizado en las peticiones de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDTO {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private LocalDateTime date;

    private List<Long> userIds;
}