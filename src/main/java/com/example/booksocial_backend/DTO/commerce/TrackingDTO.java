package com.example.booksocial_backend.DTO.commerce;

import java.time.LocalDateTime;

import com.example.booksocial_backend.domain.commerce.TrackingStatus;

/**
 * DTO de salida para la entidad Tracking.
 *
 * Representa el estado logístico de un pedido en un momento concreto,
 * incluyendo el identificador del pedido asociado.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record TrackingDTO(

    Long id,

    TrackingStatus trackingStatus,

    LocalDateTime date,

    Long orderId

) {
}