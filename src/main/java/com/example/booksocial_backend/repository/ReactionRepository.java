package com.example.booksocial_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.social.Reaction;

/**
 * Repositorio de acceso a datos para la entidad {@link Reaction}.
 *
 * Gestiono la persistencia de las reacciones dentro del sistema BookSocial,
 * proporcionando operaciones CRUD básicas heredadas de JpaRepository
 * y métodos de consulta necesarios para la interacción social sobre
 * comentarios.
 *
 * Este repositorio permite obtener las reacciones de un comentario y comprobar
 * si un usuario ya ha reaccionado previamente, garantizando la lógica de "like"
 * única por usuario y comentario.
 *
 * @author Jorge
 * @since 22/03/2026
 * @version 1.1
 */

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

  /**
   * Obtiene las reacciones asociadas a un comentario.
   *
   * @param commentId identificador del comentario
   * @return lista de reacciones del comentario
   */
  List<Reaction> findByCommentId(Long commentId);

  /**
   * Obtiene la reacción de un usuario sobre un comentario concreto.
   *
   * Permite verificar si el usuario ya ha dado "like".
   *
   * @param userId    identificador del usuario
   * @param commentId identificador del comentario
   * @return Optional con la reacción si existe
   */
  Optional<Reaction> findByUserIdAndCommentId(Long userId, Long commentId);

  /**
   * Comprueba si un usuario ha reaccionado a un comentario.
   *
   * @param userId    identificador del usuario
   * @param commentId identificador del comentario
   * @return true si existe reacción, false en caso contrario
   */
  boolean existsByUserIdAndCommentId(Long userId, Long commentId);

}