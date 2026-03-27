package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.commerce.Tracking;
import com.example.booksocial_backend.domain.commerce.TrackingStatus;
import com.example.booksocial_backend.DTO.commerce.TrackingRequestDTO;
import com.example.booksocial_backend.DTO.commerce.TrackingResponseDTO;
import com.example.booksocial_backend.domain.commerce.Order;
import com.example.booksocial_backend.repository.OrderRepository;
import com.example.booksocial_backend.repository.TrackingRepository;
import com.example.booksocial_backend.service.TrackingService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link TrackingService}.
 *
 * Gestiona la lógica de negocio del seguimiento de pedidos,
 * controlando la evolución de estados y garantizando
 * la coherencia temporal.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TrackingServiceImpl implements TrackingService {

  private final TrackingRepository trackingRepository;
  private final OrderRepository orderRepository;

  @Override
  public TrackingResponseDTO addTracking(TrackingRequestDTO request) {

    Tracking tracking = new Tracking();

    tracking.setStatus(request.getTrackingStatus());
    tracking.setDate(LocalDateTime.now());
    Order order = orderRepository.findById(request.getOrderId())
        .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + request.getOrderId()));

    tracking.setOrder(order);

    validateTracking(tracking);

    validateStateTransition(request.getOrderId(), request.getTrackingStatus());

    Tracking saved = trackingRepository.save(tracking);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public TrackingResponseDTO getTrackingById(Long id) {

    return mapToDTO(getTrackingEntityById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<TrackingResponseDTO> getTrackingByOrder(Long orderId) {

    return trackingRepository.findByOrderId(orderId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<TrackingResponseDTO> getTrackingByOrderOrdered(Long orderId) {

    return trackingRepository.findByOrderIdOrderByDateAsc(orderId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public void deleteTracking(Long id) {

    trackingRepository.delete(getTrackingEntityById(id));
  }

  private TrackingResponseDTO mapToDTO(Tracking tracking) {

    return new TrackingResponseDTO(
        tracking.getId(),
        tracking.getStatus(),
        tracking.getDate(),
        tracking.getOrder().getId());
  }

  private Tracking getTrackingEntityById(Long id) {

    return trackingRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Tracking no encontrado con id: " + id));
  }

  /**
   * Valida los datos básicos de tracking.
   */
  private void validateTracking(Tracking tracking) {

    if (tracking == null) {
      throw new IllegalArgumentException("El tracking no puede ser nulo");
    }

    if (tracking.getStatus() == null) {
      throw new IllegalArgumentException("El estado es obligatorio");
    }

    if (tracking.getOrder() == null || tracking.getOrder().getId() == null) {
      throw new IllegalArgumentException("Debe existir un pedido válido");
    }
  }

  /**
   * Valida la transición de estados de un pedido.
   */
  private void validateStateTransition(Long orderId, TrackingStatus newStatus) {

    List<Tracking> history = trackingRepository.findByOrderIdOrderByDateAsc(orderId);

    if (history.isEmpty()) {
      return;
    }

    TrackingStatus lastStatus = history.get(history.size() - 1).getStatus();

    if (lastStatus == TrackingStatus.DELIVERED) {
      throw new IllegalArgumentException("El pedido ya ha sido entregado");
    }

    if (lastStatus == TrackingStatus.CANCELED) {
      throw new IllegalArgumentException("El pedido está cancelado");
    }
  }
}