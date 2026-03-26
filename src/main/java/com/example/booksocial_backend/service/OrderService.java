package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.commerce.OrderRequestDTO;
import com.example.booksocial_backend.DTO.commerce.OrderResponseDTO;

/**
 * Servicio encargado de la gestión de pedidos dentro del sistema BookSocial.
 *
 * Un pedido representa una compra realizada por un usuario e incluye
 * múltiples líneas de pedido con los productos adquiridos.
 *
 * Este servicio se encarga de validar la coherencia del pedido,
 * calcular su importe total y gestionar su persistencia.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
public interface OrderService {

  OrderResponseDTO createOrder(OrderRequestDTO request);

  OrderResponseDTO getOrderById(Long id);

  List<OrderResponseDTO> getAllOrders();

  List<OrderResponseDTO> getOrdersByUser(Long userId);

  void deleteOrder(Long id);
}