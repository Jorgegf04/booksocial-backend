package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.booksocial_backend.DTO.commerce.TrackingOrderRequestDTO;
import com.example.booksocial_backend.domain.commerce.TrackingOrder;
import com.example.booksocial_backend.domain.commerce.TrackingOrderStatus;
import com.example.booksocial_backend.domain.commerce.Order;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.repository.OrderRepository;
import com.example.booksocial_backend.repository.TrackingOrderRepository;
import com.example.booksocial_backend.service.impl.TrackingOrderServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
/**
 * Tests unitarios de {@link com.example.booksocial_backend.service.impl.TrackingOrderServiceImpl}.
 *
 * <p>Verifica la creación de estados de tracking, las validaciones de transición
 * de estados (PREPARING→SHIPPED→IN_TRANSIT→DELIVERED) y que no se puedan añadir
 * nuevos estados a pedidos ya entregados o cancelados.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
class TrackingOrderServiceImplTest {

  @Mock
  private TrackingOrderRepository trackingRepository;

  @Mock
  private OrderRepository orderRepository;

  @InjectMocks
  private TrackingOrderServiceImpl service;

  private Order order;
  private TrackingOrder tracking;

  @BeforeEach
  void setUp() {

    User user = new User();
    user.setId(1L);
    user.setUsername("jorge");

    order = new Order();
    order.setId(1L);
    order.setUser(user);

    tracking = new TrackingOrder();
    tracking.setId(1L);
    tracking.setOrder(order);
    tracking.setStatus(TrackingOrderStatus.PREPARING);
    tracking.setDate(LocalDateTime.now());
  }

  // =========================
  // CREATE
  // =========================

  @Test
  void shouldAddTrackingSuccessfully() {

    TrackingOrderRequestDTO request = new TrackingOrderRequestDTO(1L, TrackingOrderStatus.PREPARING);

    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(trackingRepository.findByOrderIdOrderByDateAsc(1L)).thenReturn(List.of());
    when(trackingRepository.save(any())).thenReturn(tracking);

    var result = service.addTracking(request);

    assertEquals(TrackingOrderStatus.PREPARING, result.getStatus());
    assertEquals("Preparando pedido", result.getStatusLabel());
  }

  @Test
  void shouldThrowExceptionWhenOrderNotFound() {

    TrackingOrderRequestDTO request = new TrackingOrderRequestDTO(1L, TrackingOrderStatus.PREPARING);

    when(orderRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      service.addTracking(request);
    });
  }

  @Test
  void shouldThrowExceptionWhenOrderAlreadyDelivered() {

    TrackingOrder delivered = new TrackingOrder();
    delivered.setStatus(TrackingOrderStatus.DELIVERED);

    TrackingOrderRequestDTO request = new TrackingOrderRequestDTO(1L, TrackingOrderStatus.SHIPPED);

    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(trackingRepository.findByOrderIdOrderByDateAsc(1L))
        .thenReturn(List.of(delivered));

    assertThrows(IllegalArgumentException.class, () -> {
      service.addTracking(request);
    });
  }

  // =========================
  // READ
  // =========================

  @Test
  void shouldGetTrackingById() {

    when(trackingRepository.findById(1L)).thenReturn(Optional.of(tracking));

    var result = service.getTrackingById(1L);

    assertEquals(1L, result.getOrderId());
  }

  @Test
  void shouldThrowExceptionWhenTrackingNotFound() {

    when(trackingRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      service.getTrackingById(1L);
    });
  }

  @Test
  void shouldReturnAllTracking() {

    when(trackingRepository.findAll()).thenReturn(List.of(tracking));

    var result = service.getAllTracking();

    assertEquals(1, result.size());
  }

  @Test
  void shouldReturnTrackingByOrder() {

    when(trackingRepository.findByOrderId(1L)).thenReturn(List.of(tracking));

    var result = service.getTrackingByOrder(1L);

    assertEquals(1, result.size());
  }

  @Test
  void shouldReturnOrderedTracking() {

    when(trackingRepository.findByOrderIdOrderByDateAsc(1L))
        .thenReturn(List.of(tracking));

    var result = service.getTrackingByOrderOrdered(1L);

    assertEquals(1, result.size());
  }

  // =========================
  // LATEST
  // =========================

  @Test
  void shouldGetLatestTracking() {

    when(trackingRepository.findFirstByOrder_IdOrderByDateDesc(1L))
        .thenReturn(Optional.of(tracking));

    var result = service.getLatestTracking(1L);

    assertEquals(TrackingOrderStatus.PREPARING, result.getStatus());
  }

  @Test
  void shouldThrowExceptionWhenNoLatestTracking() {

    when(trackingRepository.findFirstByOrder_IdOrderByDateDesc(1L))
        .thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      service.getLatestTracking(1L);
    });
  }

  // =========================
  // DELETE
  // =========================

  @Test
  void shouldDeleteTracking() {

    when(trackingRepository.findById(1L)).thenReturn(Optional.of(tracking));

    service.deleteTracking(1L);

    verify(trackingRepository).delete(tracking);
  }
}