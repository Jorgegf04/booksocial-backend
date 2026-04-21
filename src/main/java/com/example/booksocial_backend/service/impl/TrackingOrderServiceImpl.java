package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.booksocial_backend.domain.commerce.TrackingOrder;
import com.example.booksocial_backend.domain.commerce.TrackingOrderStatus;
import com.example.booksocial_backend.DTO.commerce.TrackingOrderRequestDTO;
import com.example.booksocial_backend.DTO.commerce.TrackingOrderResponseDTO;
import com.example.booksocial_backend.domain.commerce.Order;
import com.example.booksocial_backend.repository.OrderRepository;
import com.example.booksocial_backend.repository.TrackingOrderRepository;
import com.example.booksocial_backend.service.TrackingOrderService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link TrackingOrderService}.
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
public class TrackingOrderServiceImpl implements TrackingOrderService {

  private final TrackingOrderRepository trackingRepository;
  private final OrderRepository orderRepository;

  @Override
  public TrackingOrderResponseDTO addTracking(TrackingOrderRequestDTO request) {

    TrackingOrder tracking = new TrackingOrder();

    tracking.setStatus(request.getTrackingStatus());
    tracking.setDate(LocalDateTime.now());
    Order order = orderRepository.findById(request.getOrderId())
        .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + request.getOrderId()));

    tracking.setOrder(order);

    validateTracking(tracking);

    validateStateTransition(request.getOrderId(), request.getTrackingStatus());

    TrackingOrder saved = trackingRepository.save(tracking);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public TrackingOrderResponseDTO getTrackingById(Long id) {

    return mapToDTO(getTrackingEntityById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<TrackingOrderResponseDTO> getAllTracking() {

    return trackingRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<TrackingOrderResponseDTO> getTrackingByOrder(Long orderId) {

    return trackingRepository.findByOrderId(orderId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<TrackingOrderResponseDTO> getTrackingByOrderOrdered(Long orderId) {

    return trackingRepository.findByOrderIdOrderByDateAsc(orderId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public void deleteTracking(Long id) {

    trackingRepository.delete(getTrackingEntityById(id));
  }

  private TrackingOrderResponseDTO mapToDTO(TrackingOrder tracking) {

    return new TrackingOrderResponseDTO(
        tracking.getId(),
        tracking.getStatus(),
        mapStatusLabel(tracking.getStatus()),
        tracking.getDate(),
        tracking.getOrder().getId(),

        tracking.getOrder().getUser().getId(),
        tracking.getOrder().getUser().getUsername(),

        tracking.getStatus() == TrackingOrderStatus.DELIVERED);
  }

  private String mapStatusLabel(TrackingOrderStatus status) {
    return switch (status) {
      case PREPARING -> "Preparando pedido";
      case SHIPPED -> "Enviado";
      case IN_TRANSIT -> "En tránsito";
      case DELIVERED -> "Entregado";
      case CANCELED -> "Cancelado";
    };
  }

  private TrackingOrder getTrackingEntityById(Long id) {

    return trackingRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Tracking no encontrado con id: " + id));
  }

  /**
   * Valida los datos básicos de tracking.
   */
  private void validateTracking(TrackingOrder tracking) {

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
  private void validateStateTransition(Long orderId, TrackingOrderStatus newStatus) {

    List<TrackingOrder> history = trackingRepository.findByOrderIdOrderByDateAsc(orderId);

    if (history.isEmpty()) {
      return;
    }

    TrackingOrderStatus lastStatus = history.get(history.size() - 1).getStatus();

    if (lastStatus == TrackingOrderStatus.DELIVERED) {
      throw new IllegalArgumentException("El pedido ya ha sido entregado");
    }

    if (lastStatus == TrackingOrderStatus.CANCELED) {
      throw new IllegalArgumentException("El pedido está cancelado");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public TrackingOrderResponseDTO getLatestTracking(Long orderId) {

    return trackingRepository
        .findFirstByOrder_IdOrderByDateDesc(orderId)
        .map(this::mapToDTO)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "No hay tracking para el pedido: " + orderId));
  }
}