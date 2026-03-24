package com.example.booksocial_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.user.Subscription;

/**
 * Repositorio de acceso a datos para la entidad {@link Subscription}.
 *
 * Gestiono la persistencia de las suscripciones dentro del sistema BookSocial,
 * proporcionando operaciones CRUD básicas heredadas de JpaRepository
 * y métodos de consulta necesarios para la gestión de suscripciones
 * asociadas a los usuarios.
 *
 * Este repositorio permite comprobar el estado de la suscripción de un usuario,
 * así como recuperar su información para aplicar funcionalidades específicas
 * como descuentos o acceso a contenido exclusivo.
 *
 * @author Jorge
 * @since 22/03/2026
 * @version 1.1
 */

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  /**
   * Busca la suscripción asociada a un usuario concreto.
   *
   * @param userId identificador del usuario
   * @return Optional con la suscripción si existe
   */
  Optional<Subscription> findByUserId(Long userId);

  /**
   * Comprueba si un usuario tiene una suscripción activa.
   *
   * @param userId identificador del usuario
   * @return true si el usuario tiene suscripción activa
   */
  boolean existsByUserIdAndActivatedTrue(Long userId);

}