package com.example.booksocial_backend.domain.commerce;

/**
 * Estados del ciclo de vida logístico de un pedido en BookSocial.
 *
 * <p>La transición válida es lineal y unidireccional:</p>
 * <pre>
 *   PREPARING → SHIPPED → IN_TRANSIT → DELIVERED
 *                               ↘
 *                            CANCELED (desde cualquier estado previo a DELIVERED)
 * </pre>
 * <p>El servicio {@code TrackingOrderServiceImpl} valida que no se puedan añadir
 * nuevos estados a un pedido ya entregado o cancelado.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
public enum TrackingOrderStatus {
  CANCELED,
  PREPARING,
  SHIPPED,
  IN_TRANSIT,
  DELIVERED
}
