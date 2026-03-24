package com.example.booksocial_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.catalog.Chapter;

/**
 * Repositorio de acceso a datos para la entidad {@link Chapter}.
 *
 * Gestiono la persistencia de los capítulos dentro del sistema BookSocial,
 * proporcionando operaciones CRUD básicas heredadas de JpaRepository
 * y métodos de consulta necesarios para recuperar los capítulos
 * asociados a un tomo concreto.
 *
 * Este repositorio se utiliza principalmente en la gestión de estructuras
 * de obras tipo manga, donde los capítulos se organizan jerárquicamente
 * dentro de tomos.
 *
 * @author Jorge
 * @since 22/03/2026
 * @version 1.1
 */

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

  /**
   * Obtiene los capítulos pertenecientes a un tomo.
   *
   * @param tomeId identificador del tomo
   * @return lista de capítulos asociados al tomo
   */
  List<Chapter> findByTomeId(Long tomeId);

}