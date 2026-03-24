package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.commerce.Tracking;
import com.example.booksocial_backend.domain.commerce.TrackingStatus;
import com.example.booksocial_backend.DTO.commerce.CreateTrackingRequest;
import com.example.booksocial_backend.DTO.commerce.TrackingDTO;
import com.example.booksocial_backend.domain.commerce.Order;

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

  @Override
  public TrackingDTO addTracking(CreateTrackingRequest request) {

    Tracking tracking = new Tracking();

    tracking.setTrackingStatus(request.trackingStatus());
    tracking.setDate(LocalDateTime.now());
    tracking.setOrder(Order.builder().id(request.orderId()).build());

    validateTracking(tracking);

    validateStateTransition(request.orderId(), request.trackingStatus());

    Tracking saved = trackingRepository.save(tracking);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public TrackingDTO getTrackingById(Long id) {

    return mapToDTO(getTrackingEntityById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<TrackingDTO> getTrackingByOrder(Long orderId) {

    return trackingRepository.findByOrderId(orderId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<TrackingDTO> getTrackingByOrderOrdered(Long orderId) {

    return trackingRepository.findByOrderIdOrderByDateAsc(orderId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public void deleteTracking(Long id) {

    trackingRepository.delete(getTrackingEntityById(id));
  }

  private TrackingDTO mapToDTO(Tracking tracking) {

    return new TrackingDTO(
        tracking.getId(),
        tracking.getTrackingStatus(),
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

    if (tracking.getTrackingStatus() == null) {
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

    TrackingStatus lastStatus = history.get(history.size() - 1).getTrackingStatus();

    if (lastStatus == TrackingStatus.DELIVERED) {
      throw new IllegalArgumentException("El pedido ya ha sido entregado");
    }

    if (lastStatus == TrackingStatus.CANCELED) {
      throw new IllegalArgumentException("El pedido está cancelado");
    }
  }
}