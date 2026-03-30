package com.example.booksocial_backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.commerce.OrderLineRequestDTO;
import com.example.booksocial_backend.DTO.commerce.OrderLineResponseDTO;
import com.example.booksocial_backend.domain.catalog.Product;
import com.example.booksocial_backend.domain.commerce.Order;
import com.example.booksocial_backend.domain.commerce.OrderLine;
import com.example.booksocial_backend.repository.OrderLineRepository;
import com.example.booksocial_backend.repository.OrderRepository;
import com.example.booksocial_backend.repository.ProductRepository;
import com.example.booksocial_backend.service.OrderLineService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link OrderLineService}.
 *
 * Gestiona la lógica de negocio relacionada con las líneas de pedido,
 * asegurando la integridad de los datos dentro del sistema de compra.
 *
 * ✔ No expone entidades directamente
 * ✔ Calcula el precio en backend (seguridad)
 * ✔ Valida stock
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderLineServiceImpl implements OrderLineService {

  private final OrderLineRepository orderLineRepository;
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;

  // =========================
  // CREATE
  // =========================

  @Override
  public OrderLineResponseDTO createOrderLine(OrderLineRequestDTO request) {

    // 1. Validación básica
    if (request == null) {
      throw new IllegalArgumentException("La petición no puede ser nula");
    }

    if (request.getProductId() == null) {
      throw new IllegalArgumentException("El productId es obligatorio");
    }

    if (request.getOrderId() == null) {
      throw new IllegalArgumentException("El orderId es obligatorio");
    }

    if (request.getQuantity() == null || request.getQuantity() <= 0) {
      throw new IllegalArgumentException("Cantidad inválida");
    }

    // 2. Buscar producto
    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + request.getProductId()));

    // 🔥 CONTROL DE STOCK (MUY IMPORTANTE)
    if (product.getStock() < request.getQuantity()) {
      throw new IllegalArgumentException("Stock insuficiente");
    }

    // 3. Buscar pedido
    Order order = orderRepository.findById(request.getOrderId())
        .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + request.getOrderId()));

    // 4. Construir entidad
    OrderLine line = new OrderLine();
    line.setProduct(product);
    line.setOrder(order);
    line.setQuantity(request.getQuantity());

    // 🔐 Seguridad: precio SIEMPRE backend
    line.setUnitaryPrice(product.getPrice());

    // 5. Actualizar stock
    product.setStock(product.getStock() - request.getQuantity());

    // 6. Guardar
    OrderLine saved = orderLineRepository.save(line);

    // 7. Mapear a DTO
    return mapToDTO(saved);
  }

  // =========================
  // READ
  // =========================

  @Override
  @Transactional(readOnly = true)
  public OrderLineResponseDTO getOrderLineById(Long id) {

    OrderLine line = orderLineRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Línea de pedido no encontrada: " + id));

    return mapToDTO(line);
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderLineResponseDTO> getAllOrderLines() {

    return orderLineRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderLineResponseDTO> getOrderLinesByOrder(Long orderId) {

    return orderLineRepository.findByOrderId(orderId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  // =========================
  // DELETE
  // =========================

  @Override
  public void deleteOrderLine(Long id) {

    OrderLine line = orderLineRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Línea de pedido no encontrada: " + id));

    orderLineRepository.delete(line);
  }

  // =========================
  // MAPPER
  // =========================

  private OrderLineResponseDTO mapToDTO(OrderLine line) {

    return new OrderLineResponseDTO(
        line.getProduct().getId(),
        line.getProduct().getEdition().getTitle(),
        line.getUnitaryPrice(),
        line.getQuantity(),
        line.getUnitaryPrice() * line.getQuantity());
  }
}