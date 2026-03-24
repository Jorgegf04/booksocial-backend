package com.example.booksocial_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.catalog.Editorial;

/**
 * Repositorio de acceso a datos para la entidad {@link Event}.
 *
 * Gestiono la persistencia de los eventos dentro del sistema BookSocial,
 * proporcionando operaciones CRUD básicas heredadas de JpaRepository.
 *
 * Este repositorio permite administrar los eventos exclusivos disponibles
 * para los usuarios suscritos, facilitando su creación, consulta y eliminación.
 *
 * @author Jorge
 * @since 22/03/2026
 * @version 1.1
 */

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, Long> {

  /**
   * Busca una editorial por su nombre exacto.
   *
   * @param name nombre de la editorial
   * @return Optional con la editorial si existe
   */
  Optional<Editorial> findByName(String name);

  /**
   * Busca editoriales por nombre (búsqueda parcial).
   *
   * @param name fragmento del nombre
   * @return lista de editoriales coincidentes
   */
  List<Editorial> findByNameContainingIgnoreCase(String name);

  /**
   * Busca editoriales por país de origen.
   *
   * @param country país a filtrar
   * @return lista de editoriales del país indicado
   */
  List<Editorial> findByCountryIgnoreCase(String country);

  /**
   * Comprueba si ya existe una editorial con el nombre indicado.
   *
   * @param name nombre de la editorial
   * @return true si ya existe, false en caso contrario
   */
  boolean existsByName(String name);

  /**
   * Obtiene todas las editoriales ordenadas alfabéticamente.
   *
   * @return lista de editoriales ordenadas por nombre
   */
  List<Editorial> findAllByOrderByNameAsc();
}