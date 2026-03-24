package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.commerce.Order;
import com.example.booksocial_backend.domain.commerce.OrderLine;
import com.example.booksocial_backend.DTO.commerce.CreateOrderRequest;
import com.example.booksocial_backend.DTO.commerce.OrderDTO;
import com.example.booksocial_backend.DTO.commerce.OrderLineDTO;
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
  public OrderDTO createOrder(CreateOrderRequest request) {

    Order order = new Order();

    // Set usuario
    order.setUser(User.builder().id(request.userId()).build());

    // Fecha automática
    order.setDate(LocalDateTime.now());

    // Map líneas
    List<OrderLine> lines = request.orderLines().stream()
        .map(dto -> OrderLine.builder()
            .product(Product.builder().id(dto.productId()).build())
            .quantity(dto.quantity())
            .unitaryPrice(dto.unitaryPrice())
            .order(order)
            .build())
        .toList();

    order.setOrderLines(lines);

    validateOrder(order);

    double total = calculateTotal(lines);
    order.setTotal(total);

    Order saved = orderRepository.save(order);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public OrderDTO getOrderById(Long id) {

    return mapToDTO(getOrderEntityById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderDTO> getAllOrders() {

    return orderRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderDTO> getOrdersByUser(Long userId) {

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

  private OrderDTO mapToDTO(Order order) {

    return new OrderDTO(
        order.getId(),
        order.getDate(),
        order.getTotal(),
        order.getUser().getId(),
        order.getOrderLines().stream()
            .map(l -> new OrderLineDTO(
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