package com.example.booksocial_backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.commerce.OrderLine;
import com.example.booksocial_backend.repository.OrderLineRepository;
import com.example.booksocial_backend.service.OrderLineService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link OrderLineService}.
 *
 * Gestiona la lógica de negocio relacionada con las líneas de pedido,
 * asegurando la integridad de los datos dentro del sistema de compra.
 *
 * Controla la cantidad de productos y el precio unitario en el momento
 * de la compra.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 2.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderLineServiceImpl implements OrderLineService {

  private final OrderLineRepository orderLineRepository;

  @Override
  public OrderLine createOrderLine(OrderLine orderLine) {

    validateOrderLine(orderLine);

    // Seguridad: el precio NO debería venir del frontend
    if (orderLine.getUnitaryPrice() == null || orderLine.getUnitaryPrice() < 0) {
      throw new IllegalArgumentException("El precio unitario no es válido");
    }

    // Normalización básica
    if (orderLine.getQuantity() <= 0) {
      throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
    }

    return orderLineRepository.save(orderLine);
  }

  @Override
  @Transactional(readOnly = true)
  public OrderLine getOrderLineById(Long id) {

    return orderLineRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Línea de pedido no enco" + id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderLine> getAllOrderLines() {

    return orderLineRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderLine> getOrderLinesByOrder(Long orderId) {

    return orderLineRepository.findByOrderId(orderId);
  }

  @Override
  public void deleteOrderLine(Long id) {

    OrderLine orderLine = getOrderLineById(id);

    orderLineRepository.delete(orderLine);
  }

  /**
   * Valida los datos básicos de una línea de pedido.
   *
   * @param orderLine línea a validar
   */
  private void validateOrderLine(OrderLine orderLine) {

    if (orderLine == null) {
      throw new IllegalArgumentException("La línea de pedido no puede ser nula");
    }

    if (orderLine.getProduct() == null || orderLine.getProduct().getId() == null) {
      throw new IllegalArgumentException("Debe existir un producto válido");
    }

    if (orderLine.getOrder() == null || orderLine.getOrder().getId() == null) {
      throw new IllegalArgumentException("Debe existir un pedido válido");
    }

    if (orderLine.getQuantity() == null || orderLine.getQuantity() <= 0) {
      throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
    }
  }
}