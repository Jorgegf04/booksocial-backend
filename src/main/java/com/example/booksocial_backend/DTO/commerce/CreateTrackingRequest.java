package com.example.booksocial_backend.DTO.commerce;

import com.example.booksocial_backend.domain.commerce.TrackingStatus;

/**
 * DTO de entrada para la creación de estados de tracking.
 *
 * Permite registrar un nuevo estado logístico para un pedido.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record CreateTrackingRequest(

    Long orderId,

    TrackingStatus trackingStatus

) {
}