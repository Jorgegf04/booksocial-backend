package com.example.booksocial_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.catalog.Volume;

/**
 * Repositorio de acceso a datos para la entidad {@link Volume}.
 *
 * Gestiono la persistencia de los volúmenes dentro del sistema BookSocial,
 * proporcionando operaciones CRUD básicas heredadas de JpaRepository
 * y métodos de consulta necesarios para recuperar los volúmenes asociados
 * a una edición concreta.
 *
 * Este repositorio se utiliza en la gestión de obras tipo cómic,
 * permitiendo acceder a la estructura de volúmenes que componen una colección.
 *
 * @author Jorge
 * @since 22/03/2026
 * @version 1.1
 */

@Repository
public interface VolumeRepository extends JpaRepository<Volume, Long> {

  /**
   * Busca volúmenes pertenecientes a una edición.
   *
   * @param editionId identificador de la edición
   * @return lista de volúmenes asociados
   */
  List<Volume> findByEditionId(Long editionId);

  /**
   * Busca volúmenes de una edición ordenados por número.
   *
   * @param editionId identificador de la edición
   * @return lista de volúmenes ordenados
   */
  List<Volume> findByEditionIdOrderByVolumeNumberAsc(Long editionId);

}