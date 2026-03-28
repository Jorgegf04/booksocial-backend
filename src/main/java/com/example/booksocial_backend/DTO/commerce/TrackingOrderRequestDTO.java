package com.example.booksocial_backend.DTO.commerce;

import com.example.booksocial_backend.domain.commerce.TrackingOrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para la creación de estados de tracking.
 *
 * Permite registrar un nuevo estado logístico para un pedido,
 * indicando el pedido asociado y su nuevo estado.
 *
 * Este DTO es utilizado en las peticiones de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingOrderRequestDTO {

    private Long orderId;

    private TrackingOrderStatus trackingStatus;
}