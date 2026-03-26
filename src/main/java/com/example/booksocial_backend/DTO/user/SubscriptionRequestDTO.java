package com.example.booksocial_backend.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada para activar una suscripción.
 *
 * Permite identificar el usuario que desea activar
 * una suscripción dentro del sistema.
 *
 * Utilizado en peticiones de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequestDTO {

    @NotNull
    private Long userId;

}