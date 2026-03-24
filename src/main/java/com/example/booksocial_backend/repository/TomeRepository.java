package com.example.booksocial_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.catalog.Tome;

/**
 * Repositorio de acceso a datos para la entidad {@link Tome}.
 *
 * Gestiono la persistencia de los tomos dentro del sistema BookSocial,
 * proporcionando operaciones CRUD básicas heredadas de JpaRepository
 * y métodos de consulta necesarios para recuperar los tomos asociados
 * a una edición concreta.
 *
 * Este repositorio se utiliza principalmente en la gestión de obras tipo manga,
 * permitiendo acceder a la estructura de tomos que agrupan los capítulos.
 *
 * @author Jorge
 * @since 22/03/2026
 * @version 1.1
 */

@Repository
public interface TomeRepository extends JpaRepository<Tome, Long> {

  /**
   * Busca tomos por edición.
   *
   * @param editionId identificador de la edición
   * @return lista de tomos asociados
   */
  List<Tome> findByEditionId(Long editionId);

  /**
   * Busca tomos por edición ordenados por número.
   *
   * @param editionId identificador de la edición
   * @return lista de tomos ordenados
   */
  List<Tome> findByEditionIdOrderByNumberTomeAsc(Long editionId);

}