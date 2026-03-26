package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.commerce.TrackingRequestDTO;
import com.example.booksocial_backend.DTO.commerce.TrackingResponseDTO;

/**
 * Servicio encargado del seguimiento de pedidos dentro del sistema.
 *
 * Permite registrar y consultar el estado logístico de un pedido,
 * manteniendo un historial ordenado de cambios.
 *
 * Controla la coherencia de los estados y la evolución del pedido.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
public interface TrackingService {

  TrackingResponseDTO addTracking(TrackingRequestDTO request);

  TrackingResponseDTO getTrackingById(Long id);

  List<TrackingResponseDTO> getTrackingByOrder(Long orderId);

  List<TrackingResponseDTO> getTrackingByOrderOrdered(Long orderId);

  void deleteTracking(Long id);
}