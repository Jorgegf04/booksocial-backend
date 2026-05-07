package com.example.booksocial_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.commerce.OrderLine;

/**
 * Repositorio de acceso a datos para la entidad {@link OrderLine}.
 *
 * Gestiono la persistencia de las líneas de pedido dentro del sistema
 * BookSocial,
 * proporcionando operaciones CRUD básicas heredadas de JpaRepository
 * y métodos de consulta necesarios para recuperar las líneas asociadas
 * a un pedido concreto.
 *
 * Este repositorio forma parte del sistema de compra, permitiendo
 * obtener el detalle de productos incluidos en cada pedido.
 *
 * @author Jorge
 * @since 22/03/2026
 * @version 1.1
 */

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

  /**
   * Obtiene las líneas pertenecientes a un pedido.
   *
   * @param orderId identificador del pedido
   * @return lista de líneas del pedido
   */
  List<OrderLine> findByOrderId(Long orderId);

  boolean existsByProductId(Long productId);
}