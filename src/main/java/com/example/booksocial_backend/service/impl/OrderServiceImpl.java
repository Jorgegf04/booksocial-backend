package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.commerce.Order;
import com.example.booksocial_backend.domain.commerce.OrderLine;
import com.example.booksocial_backend.DTO.commerce.OrderLineResponseDTO;
import com.example.booksocial_backend.DTO.commerce.OrderRequestDTO;
import com.example.booksocial_backend.DTO.commerce.OrderResponseDTO;
import com.example.booksocial_backend.domain.catalog.Product;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.exception.OrderNotFoundException;
import com.example.booksocial_backend.exception.ProductNotFoundException;

import com.example.booksocial_backend.repository.OrderRepository;
import com.example.booksocial_backend.repository.ProductRepository;
import com.example.booksocial_backend.repository.UserRepository;
import com.example.booksocial_backend.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

  private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  @Override
  public OrderResponseDTO createOrder(OrderRequestDTO request) {

    log.info("[ORDER] [CREATE] [START] userId={}", request.getUserId());

    if (request.getOrderLines() == null || request.getOrderLines().isEmpty()) {
      throw new IllegalArgumentException("El pedido debe contener al menos una línea de pedido");
    }

    User user = userRepository.getReferenceById(request.getUserId());

    Order order = Order.builder()
        .user(user)
        .date(LocalDateTime.now())
        .build();

    List<OrderLine> lines = request.getOrderLines().stream()
        .map(dto -> {
          Product product = productRepository.findById(dto.getProductId())
              .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con id: " + dto.getProductId()));

          if (product.getStock() == null || product.getStock() < dto.getQuantity()) {
            log.warn("[ORDER] [CREATE] [WARN] Stock insuficiente para productId={}", product.getId());
            throw new IllegalArgumentException("Stock insuficiente para el producto con id: " + product.getId());
          }

          product.setStock(product.getStock() - dto.getQuantity());
          productRepository.save(product);

          return OrderLine.builder()
              .product(product)
              .quantity(dto.getQuantity())
              .unitaryPrice(product.getPrice())
              .order(order)
              .build();
        })
        .toList();

    order.setOrderLines(new ArrayList<>(lines));

    double total = calculateTotal(lines);
    order.setTotal(total);

    Order saved = orderRepository.save(order);
    log.info("[ORDER] [CREATE] [SUCCESS] id={} total={}", saved.getId(), saved.getTotal());
    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public OrderResponseDTO getOrderById(Long id) {
    log.info("[ORDER] [READ] id={}", id);
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
    log.info("[ORDER] [DELETE] [START] id={}", id);
    Order order = getOrderEntityById(id);
    orderRepository.delete(order);
    log.info("[ORDER] [DELETE] [SUCCESS] id={}", id);
  }

  private double calculateTotal(List<OrderLine> lines) {
    return lines.stream()
        .mapToDouble(l -> l.getUnitaryPrice() * l.getQuantity())
        .sum();
  }

  private OrderResponseDTO mapToDTO(Order order) {

    List<OrderLine> safeLines = order.getOrderLines() != null ? order.getOrderLines() : List.of();

    List<OrderLineResponseDTO> lines = safeLines.stream()
        .map(l -> new OrderLineResponseDTO(
            l.getProduct().getId(),
            l.getProduct().getEdition() != null ? l.getProduct().getEdition().getTitle() : null,
            l.getProduct().getPrice(),
            l.getQuantity(),
            l.getQuantity() * l.getProduct().getPrice()))
        .toList();

    int totalItems = safeLines.stream()
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
        .orElseThrow(() -> new OrderNotFoundException(id));
  }
}
