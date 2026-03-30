package com.example.booksocial_backend.DTO.commerce;

import java.time.LocalDateTime;

import com.example.booksocial_backend.domain.commerce.TrackingOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la entidad Tracking.
 *
 * Representa el estado logístico de un pedido en un momento concreto,
 * incluyendo el identificador del pedido asociado y la fecha del cambio.
 *
 * Este DTO es utilizado en las respuestas de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingOrderResponseDTO {

    private Long id;

    private TrackingOrderStatus status;
    private String statusLabel;

    private LocalDateTime date;

    private Long orderId;

    private Long userId;
    private String username;

    private Boolean completed;
}