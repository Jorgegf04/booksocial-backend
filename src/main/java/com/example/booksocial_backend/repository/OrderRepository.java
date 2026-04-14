package com.example.booksocial_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.commerce.Order;

/**
 * Repositorio de acceso a datos para la entidad {@link Order}.
 *
 * Gestiono la persistencia de los pedidos dentro del sistema BookSocial,
 * proporcionando operaciones CRUD básicas heredadas de JpaRepository
 * y métodos de consulta necesarios para recuperar los pedidos realizados
 * por los usuarios.
 *
 * Este repositorio forma parte del sistema de compra, permitiendo
 * acceder al historial de pedidos y gestionar la información asociada
 * a las compras realizadas en la plataforma.
 *
 * @author Jorge
 * @since 22/03/2026
 * @version 1.1
 */

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  /**
   * Obtiene los pedidos realizados por un usuario, cargando todas las
   * asociaciones necesarias en una sola consulta para evitar LazyInitializationException.
   *
   * @param userId identificador del usuario
   * @return lista de pedidos del usuario con líneas, productos y ediciones cargados
   */
  @Query("SELECT DISTINCT o FROM Order o " +
         "LEFT JOIN FETCH o.orderLines ol " +
         "LEFT JOIN FETCH ol.product p " +
         "LEFT JOIN FETCH p.edition " +
         "WHERE o.user.id = :userId")
  List<Order> findByUserId(@Param("userId") Long userId);

}