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
import com.example.booksocial_backend.repository.ProductRepository;
import com.example.booksocial_backend.repository.UserRepository;
import com.example.booksocial_backend.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  @Override
  public OrderResponseDTO createOrder(OrderRequestDTO request) {

    if (request.getOrderLines() == null || request.getOrderLines().isEmpty()) {
      throw new IllegalArgumentException("El pedido debe contener al menos una línea de pedido");
    }

    // getReferenceById devuelve un proxy Hibernate por ID sin SELECT extra —
    // es la forma correcta de asignar un @ManyToOne cuando solo se necesita la FK.
    User user = userRepository.getReferenceById(request.getUserId());

    Order order = Order.builder()
        .user(user)
        .date(LocalDateTime.now())
        .build();

    List<OrderLine> lines = request.getOrderLines().stream()
        .map(dto -> {

          Product product = productRepository.findById(dto.getProductId())
              .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

          // ✅ Validar stock
          if (product.getStock() < dto.getQuantity()) {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + product.getId());
          }

          return OrderLine.builder()
              .product(product)
              .quantity(dto.getQuantity())
              .unitaryPrice(product.getPrice()) // 🔥 precio seguro
              .order(order)
              .build();
        })
        .toList();

    order.setOrderLines(new ArrayList<>(lines));

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
        .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
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

    List<OrderLineResponseDTO> lines = order.getOrderLines().stream()
        .map(l -> new OrderLineResponseDTO(
            l.getProduct().getId(),
            l.getProduct().getEdition().getTitle(),
            l.getProduct().getPrice(),
            l.getQuantity(),
            l.getQuantity() * l.getProduct().getPrice()))
        .toList();

    int totalItems = order.getOrderLines().stream()
        .mapToInt(OrderLine::getQuantity)
        .sum();

    return new OrderResponseDTO(
        order.getId(),
        order.getDate(),
        order.getTotal(),

        order.getUser().getId(),
        order.getUser().getUsername(),

        totalItems,

        lines);
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