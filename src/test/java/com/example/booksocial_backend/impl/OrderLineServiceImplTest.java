package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.example.booksocial_backend.DTO.commerce.OrderLineRequestDTO;
import com.example.booksocial_backend.domain.catalog.Product;
import com.example.booksocial_backend.domain.catalog.Edition;
import com.example.booksocial_backend.domain.commerce.Order;
import com.example.booksocial_backend.domain.commerce.OrderLine;
import com.example.booksocial_backend.repository.OrderLineRepository;
import com.example.booksocial_backend.repository.OrderRepository;
import com.example.booksocial_backend.repository.ProductRepository;
import com.example.booksocial_backend.service.impl.OrderLineServiceImpl;

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
 * Tests unitarios de {@link com.example.booksocial_backend.service.impl.OrderLineServiceImpl}.
 *
 * <p>Verifica la creación de líneas de pedido: comprueba que el precio unitario se toma
 * del producto en el momento de la creación, que el stock se decrementa correctamente
 * y que se lanza excepción ante stock insuficiente, producto u orden inexistentes.
 * Cubre también la búsqueda por ID y por pedido, y la eliminación de líneas.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
class OrderLineServiceImplTest {

  @Mock
  private OrderLineRepository orderLineRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private OrderRepository orderRepository;

  @InjectMocks
  private OrderLineServiceImpl service;

  private Product product;
  private Order order;
  private OrderLine orderLine;

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

    orderLine = new OrderLine();
    orderLine.setId(1L);
    orderLine.setProduct(product);
    orderLine.setOrder(order);
    orderLine.setQuantity(2);
    orderLine.setUnitaryPrice(10.0);
  }

  // =========================
  // CREATE
  // =========================

  @Test
  void shouldCreateOrderLineSuccessfully() {

    OrderLineRequestDTO request = new OrderLineRequestDTO(1L, 1L, 2);

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(orderLineRepository.save(any())).thenReturn(orderLine);

    var result = service.createOrderLine(request);

    assertEquals(1L, result.getProductId());
    assertEquals("Naruto", result.getTitle());
    assertEquals(10.0, result.getPrice());
    assertEquals(2, result.getQuantity());
    assertEquals(20.0, result.getSubtotal());

    // ✔ Stock actualizado
    assertEquals(8, product.getStock());
  }

  @Test
  void shouldThrowExceptionWhenRequestIsNull() {

    assertThrows(IllegalArgumentException.class, () -> {
      service.createOrderLine(null);
    });
  }

  @Test
  void shouldThrowExceptionWhenProductNotFound() {

    OrderLineRequestDTO request = new OrderLineRequestDTO(1L, 1L, 2);

    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      service.createOrderLine(request);
    });
  }

  @Test
  void shouldThrowExceptionWhenOrderNotFound() {

    OrderLineRequestDTO request = new OrderLineRequestDTO(1L, 1L, 2);

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(orderRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      service.createOrderLine(request);
    });
  }

  @Test
  void shouldThrowExceptionWhenStockIsInsufficient() {

    product.setStock(1);

    OrderLineRequestDTO request = new OrderLineRequestDTO(1L, 1L, 2);

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    assertThrows(IllegalArgumentException.class, () -> {
      service.createOrderLine(request);
    });
  }

  // =========================
  // READ
  // =========================

  @Test
  void shouldGetOrderLineById() {

    when(orderLineRepository.findById(1L)).thenReturn(Optional.of(orderLine));

    var result = service.getOrderLineById(1L);

    assertEquals(1L, result.getProductId());
  }

  @Test
  void shouldThrowExceptionWhenOrderLineNotFound() {

    when(orderLineRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      service.getOrderLineById(1L);
    });
  }

  @Test
  void shouldReturnAllOrderLines() {

    when(orderLineRepository.findAll()).thenReturn(List.of(orderLine));

    var result = service.getAllOrderLines();

    assertEquals(1, result.size());
  }

  @Test
  void shouldReturnOrderLinesByOrder() {

    when(orderLineRepository.findByOrderId(1L)).thenReturn(List.of(orderLine));

    var result = service.getOrderLinesByOrder(1L);

    assertEquals(1, result.size());
  }

  // =========================
  // DELETE
  // =========================

  @Test
  void shouldDeleteOrderLine() {

    when(orderLineRepository.findById(1L)).thenReturn(Optional.of(orderLine));

    service.deleteOrderLine(1L);

    verify(orderLineRepository).delete(orderLine);
  }

  @Test
  void shouldThrowExceptionWhenDeletingNonExistingOrderLine() {

    when(orderLineRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      service.deleteOrderLine(1L);
    });
  }
}