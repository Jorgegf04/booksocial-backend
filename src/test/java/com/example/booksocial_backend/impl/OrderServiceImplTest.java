package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.example.booksocial_backend.DTO.commerce.OrderLineRequestDTO;
import com.example.booksocial_backend.DTO.commerce.OrderRequestDTO;
import com.example.booksocial_backend.domain.catalog.Product;
import com.example.booksocial_backend.domain.catalog.Edition;
import com.example.booksocial_backend.domain.commerce.Order;
import com.example.booksocial_backend.repository.OrderRepository;
import com.example.booksocial_backend.repository.ProductRepository;
import com.example.booksocial_backend.service.impl.OrderServiceImpl;

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
class OrderServiceImplTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private OrderServiceImpl service;

  private Product product;
  private Order order;

  @BeforeEach
  void setUp() {

    Edition edition = new Edition();
    edition.setTitle("Naruto");

    product = new Product();
    product.setId(1L);
    product.setPrice(10.0);
    product.setStock(10);
    product.setEdition(edition);

    order = new Order();
    order.setId(1L);
  }

  // =========================
  // CREATE
  // =========================

  @Test
  void shouldCreateOrderSuccessfully() {

    OrderLineRequestDTO line = new OrderLineRequestDTO(1L, 1L, 2);

    OrderRequestDTO request = new OrderRequestDTO(
        1L,
        List.of(line));

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var result = service.createOrder(request);

    assertEquals(20.0, result.getTotal());
    assertEquals(2, result.getTotalItems());
    assertEquals(1, result.getOrderLines().size());
  }

  @Test
  void shouldThrowExceptionWhenNoOrderLines() {

    OrderRequestDTO request = new OrderRequestDTO(
        1L,
        List.of());

    assertThrows(IllegalArgumentException.class, () -> {
      service.createOrder(request);
    });
  }

  @Test
  void shouldThrowExceptionWhenProductNotFound() {

    OrderLineRequestDTO line = new OrderLineRequestDTO(1L, 1L, 2);

    OrderRequestDTO request = new OrderRequestDTO(
        1L,
        List.of(line));

    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      service.createOrder(request);
    });
  }

  @Test
  void shouldThrowExceptionWhenStockInsufficient() {

    product.setStock(1);

    OrderLineRequestDTO line = new OrderLineRequestDTO(1L, 1L, 2);

    OrderRequestDTO request = new OrderRequestDTO(
        1L,
        List.of(line));

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    assertThrows(IllegalArgumentException.class, () -> {
      service.createOrder(request);
    });
  }

  // =========================
  // READ
  // =========================

  @Test
  void shouldGetOrderById() {

    order.setOrderLines(new java.util.ArrayList<>());
    order.setUser(com.example.booksocial_backend.domain.user.User.builder().id(1L).build());

    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    var result = service.getOrderById(1L);

    assertEquals(1L, result.getUserId());
  }

  @Test
  void shouldThrowExceptionWhenOrderNotFound() {

    when(orderRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      service.getOrderById(1L);
    });
  }

  @Test
  void shouldReturnAllOrders() {

    order.setOrderLines(new java.util.ArrayList<>());
    order.setUser(com.example.booksocial_backend.domain.user.User.builder().id(1L).build());

    when(orderRepository.findAll()).thenReturn(List.of(order));

    var result = service.getAllOrders();

    assertEquals(1, result.size());
  }

  @Test
  void shouldReturnOrdersByUser() {

    order.setOrderLines(new java.util.ArrayList<>());
    order.setUser(com.example.booksocial_backend.domain.user.User.builder().id(1L).build());

    when(orderRepository.findByUserId(1L)).thenReturn(List.of(order));

    var result = service.getOrdersByUser(1L);

    assertEquals(1, result.size());
  }

  // =========================
  // DELETE
  // =========================

  @Test
  void shouldDeleteOrder() {

    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    service.deleteOrder(1L);

    verify(orderRepository).delete(order);
  }

  @Test
  void shouldThrowExceptionWhenDeletingNonExistingOrder() {

    when(orderRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      service.deleteOrder(1L);
    });
  }
}