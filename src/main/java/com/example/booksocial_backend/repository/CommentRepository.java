package com.example.booksocial_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.social.Comment;

/**
 * Repositorio de acceso a datos para la entidad {@link Comment}.
 *
 * Gestiono la persistencia de los comentarios dentro del sistema BookSocial,
 * proporcionando operaciones CRUD básicas y métodos de consulta necesarios
 * para recuperar comentarios asociados a obras y usuarios.
 *
 * Este repositorio permite gestionar el sistema social de la plataforma,
 * incluyendo la obtención de comentarios principales y respuestas dentro
 * de estructuras jerárquicas.
 *
 * @author Jorge
 * @since 22/03/2026
 * @version 1.1
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  /**
   * Obtiene los comentarios asociados a una obra.
   *
   * @param workId identificador de la obra
   * @return lista de comentarios de la obra
   */
  List<Comment> findByWorkId(Long workId);

  /**
   * Obtiene los comentarios realizados por un usuario.
   *
   * @param userId identificador del usuario
   * @return lista de comentarios del usuario
   */
  List<Comment> findByUserId(Long userId);

  /**
   * Obtiene los comentarios principales (sin padre) de una obra.
   *
   * Permite construir la estructura jerárquica de comentarios.
   *
   * @param workId identificador de la obra
   * @return lista de comentarios raíz
   */
  List<Comment> findByWorkIdAndParentIsNull(Long workId);

  /**
   * Obtiene las respuestas de un comentario.
   *
   * @param parentId identificador del comentario padre
   * @return lista de respuestas
   */
  List<Comment> findByParentId(Long parentId);

  /**
   * Obtiene comentarios ordenados por fecha descendente.
   *
   * @param workId identificador de la obra
   * @return lista de comentarios ordenados (más recientes primero)
   */
  List<Comment> findByWorkIdOrderByDateDesc(Long workId);

  /**
   * Obtiene los comentarios raíz de una obra ordenados por fecha descendente.
   *
   * @param workId identificador de la obra
   * @return lista de comentarios raíz ordenados (más recientes primero)
   */
  List<Comment> findByWorkIdAndParentIsNullOrderByDateDesc(Long workId);

}