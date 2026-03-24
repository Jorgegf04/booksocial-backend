package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.domain.commerce.OrderLine;

/**
 * Servicio encargado de la gestión de líneas de pedido dentro del sistema.
 *
 * Cada línea representa un producto incluido en un pedido, junto con su
 * cantidad y precio en el momento de la compra.
 *
 * Este servicio se encarga de garantizar la coherencia de los datos
 * dentro del sistema de compra.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 2.0
 */
public interface OrderLineService {

  OrderLine createOrderLine(OrderLine orderLine);

  OrderLine getOrderLineById(Long id);

  List<OrderLine> getAllOrderLines();

  List<OrderLine> getOrderLinesByOrder(Long orderId);

  void deleteOrderLine(Long id);
}