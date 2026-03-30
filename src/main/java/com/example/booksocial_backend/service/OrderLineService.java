package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.commerce.OrderLineRequestDTO;
import com.example.booksocial_backend.DTO.commerce.OrderLineResponseDTO;

/**
 * Servicio encargado de la gestión de líneas de pedido dentro del sistema.
 *
 * Se encarga de la lógica de negocio y evita exponer entidades directamente.
 */
public interface OrderLineService {

  OrderLineResponseDTO createOrderLine(OrderLineRequestDTO request);

  OrderLineResponseDTO getOrderLineById(Long id);

  List<OrderLineResponseDTO> getAllOrderLines();

  List<OrderLineResponseDTO> getOrderLinesByOrder(Long orderId);

  void deleteOrderLine(Long id);
}