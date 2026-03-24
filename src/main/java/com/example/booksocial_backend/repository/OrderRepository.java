package com.example.booksocial_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
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
   * Obtiene los pedidos realizados por un usuario.
   *
   * @param userId identificador del usuario
   * @return lista de pedidos del usuario
   */
  List<Order> findByUserId(Long userId);

}