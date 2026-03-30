package com.example.booksocial_backend.service;

import java.util.List;
import java.util.Optional;

import com.example.booksocial_backend.DTO.commerce.TrackingOrderRequestDTO;
import com.example.booksocial_backend.DTO.commerce.TrackingOrderResponseDTO;
import com.example.booksocial_backend.DTO.social.TrackingWorkResponseDTO;
import com.example.booksocial_backend.domain.commerce.TrackingOrder;

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
public interface TrackingOrderService {

  TrackingOrderResponseDTO addTracking(TrackingOrderRequestDTO request);

  TrackingOrderResponseDTO getTrackingById(Long id);

  List<TrackingOrderResponseDTO> getAllTracking();

  List<TrackingOrderResponseDTO> getTrackingByOrder(Long orderId);

  List<TrackingOrderResponseDTO> getTrackingByOrderOrdered(Long orderId);

  void deleteTracking(Long id);

  TrackingOrderResponseDTO getLatestTracking(Long orderId);

}