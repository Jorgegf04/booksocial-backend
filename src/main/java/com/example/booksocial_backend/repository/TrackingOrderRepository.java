package com.example.booksocial_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.commerce.TrackingOrder;

/**
 * Repositorio de acceso a datos para la entidad {@link TrackingOrder}.
 *
 * Gestiono la persistencia del seguimiento de pedidos dentro del sistema
 * BookSocial,
 * proporcionando operaciones CRUD básicas heredadas de JpaRepository
 * y métodos de consulta necesarios para recuperar el historial de estados
 * asociados a un pedido.
 *
 * Este repositorio forma parte del sistema de compra, permitiendo
 * representar la evolución logística de los pedidos mediante distintos estados.
 *
 * @author Jorge
 * @since 22/03/2026
 * @version 1.1
 */

@Repository
public interface TrackingOrderRepository extends JpaRepository<TrackingOrder, Long> {

  /**
   * Obtiene el tracking asociado a un pedido.
   *
   * @param orderId identificador del pedido
   * @return lista de estados del pedido
   */
  List<TrackingOrder> findByOrderId(Long orderId);

  /**
   * Obtiene el tracking de un pedido ordenado por fecha.
   *
   * @param orderId identificador del pedido
   * @return lista de estados ordenados cronológicamente
   */
  List<TrackingOrder> findByOrderIdOrderByDateAsc(Long orderId);

}