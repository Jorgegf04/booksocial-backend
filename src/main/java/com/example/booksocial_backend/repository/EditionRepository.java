package com.example.booksocial_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.catalog.Edition;

/**
 * Repositorio de acceso a datos para la entidad {@link Edition}.
 *
 * Gestiono la persistencia de las ediciones dentro del sistema BookSocial,
 * proporcionando operaciones CRUD básicas heredadas de JpaRepository
 * y métodos de consulta necesarios para identificar ediciones concretas.
 *
 * Este repositorio se utiliza principalmente para localizar ediciones
 * mediante su ISBN, el cual actúa como identificador único dentro del sistema.
 *
 * @author Jorge
 * @since 22/03/2026
 * @version 1.1
 */

@Repository
public interface EditionRepository extends JpaRepository<Edition, Long> {
  /**
   * Busca una edición a partir de su ISBN.
   *
   * @param isbn código ISBN de la edición
   * @return Optional con la edición si existe
   */
  Optional<Edition> findByIsbn(String isbn);

  /**
   * Devuelve la lista con las ediciones de una editorial
   *
   * @param editorailId código ISBN de la edición
   * @return Lista de ediciones de una editorial
   */
  List<Edition> findByEditorialId(Long editorialId);

}
