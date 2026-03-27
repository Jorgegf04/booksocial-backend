package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.commerce.Order;
import com.example.booksocial_backend.domain.commerce.OrderLine;
import com.example.booksocial_backend.DTO.commerce.OrderLineRequestDTO;
import com.example.booksocial_backend.DTO.commerce.OrderLineResponseDTO;
import com.example.booksocial_backend.DTO.commerce.OrderRequestDTO;
import com.example.booksocial_backend.DTO.commerce.OrderResponseDTO;
import com.example.booksocial_backend.domain.catalog.Product;
import com.example.booksocial_backend.domain.user.User;

import com.example.booksocial_backend.repository.OrderRepository;
import com.example.booksocial_backend.service.OrderService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link OrderService}.
 *
 * Gestiona la lógica de negocio del sistema de compra,
 * incluyendo la validación de pedidos, cálculo de importes
 * y control de integridad de los datos.
 *
 * Este servicio es el núcleo del módulo de comercio electrónico.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;

  @Override
  public OrderResponseDTO createOrder(OrderRequestDTO request) {

    System.out.println("REQUEST COMPLETO: " + request);
    System.out.println("ORDER LINES: " + request.getOrderLines());

    if (request.getOrderLines() == null || request.getOrderLines().isEmpty()) {
      throw new IllegalArgumentException("El pedido debe contener al menos una línea de pedido");
    }

    Order order = Order.builder()
        .user(User.builder().id(request.getUserId()).build())
        .date(LocalDateTime.now())
        .build();

    List<OrderLine> lines = request.getOrderLines().stream()
        .map(dto -> OrderLine.builder()
            .product(Product.builder().id(dto.getProductId()).build())
            .quantity(dto.getQuantity())
            .unitaryPrice(dto.getUnitaryPrice())
            .order(order)
            .build())
        .toList();

    order.setOrderLines(new ArrayList<>());
    order.getOrderLines().addAll(lines);

    validateOrder(order);

    double total = calculateTotal(lines);
    order.setTotal(total);

    Order saved = orderRepository.save(order);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public OrderResponseDTO getOrderById(Long id) {

    return mapToDTO(getOrderEntityById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderResponseDTO> getAllOrders() {

    return orderRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderResponseDTO> getOrdersByUser(Long userId) {

    return orderRepository.findByUserId(userId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public void deleteOrder(Long id) {

    orderRepository.delete(getOrderEntityById(id));
  }

  private double calculateTotal(List<OrderLine> lines) {

    return lines.stream()
        .mapToDouble(l -> l.getUnitaryPrice() * l.getQuantity())
        .sum();
  }

  private OrderResponseDTO mapToDTO(Order order) {

    return new OrderResponseDTO(
        order.getId(),
        order.getDate(),
        order.getTotal(),
        order.getUser().getId(),
        order.getOrderLines().stream()
            .map(l -> new OrderLineResponseDTO(
                l.getProduct().getId(),
                l.getQuantity(),
                l.getUnitaryPrice()))
            .toList());
  }

  private Order getOrderEntityById(Long id) {

    return orderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));
  }

  private void validateOrder(Order order) {

    if (order == null) {
      throw new IllegalArgumentException("El pedido no puede ser nulo");
    }

    if (order.getUser() == null || order.getUser().getId() == null) {
      throw new IllegalArgumentException("El pedido debe tener un usuario válido");
    }

    if (order.getOrderLines() == null || order.getOrderLines().isEmpty()) {
      throw new IllegalArgumentException("El pedido debe contener al menos una línea");
    }
  }
}