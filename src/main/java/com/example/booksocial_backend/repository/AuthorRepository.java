package com.example.booksocial_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.catalog.Author;

/**
 * Repositorio de acceso a datos para la entidad {@link Author}.
 *
 * Gestiono la persistencia de autores dentro del sistema BookSocial,
 * proporcionando operaciones CRUD básicas heredadas de JpaRepository
 * y métodos de consulta personalizados para permitir la búsqueda
 * y filtrado de autores en el catálogo.
 *
 * Estos métodos son utilizados principalmente en funcionalidades
 * de exploración, filtrado y gestión de relaciones con obras.
 *
 * @author Jorge
 * @since 12/03/2026
 * @version 1.1
 */

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
  /**
   * Busca autores cuyo nombre contenga el texto indicado (búsqueda parcial).
   * No distingue entre mayúsculas y minúsculas.
   *
   * @param name fragmento del nombre a buscar
   * @return lista de autores que coinciden con la búsqueda
   *
   * 
   */
  List<Author> findByNameContainingIgnoreCase(String name);

  /**
   * Busco un autor por su nombre exacto.
   *
   * Útil para selecciones en formularios o relaciones con otras entidades.
   *
   * @param name nombre completo del autor
   * @return autor encontrado (si existe)
   */
  Optional<Author> findByName(String name);

  /**
   * Comprueba si ya existe un autor con el nombre exacto indicado.
   *
   * @param name nombre completo del autor
   * @return true si ya existe, false en caso contrario
   */

  boolean existsByName(String name);

  /**
   * Busco autores por nombre y nacionalidad combinados.
   *
   * Permite aplicar filtros avanzados en el catálogo.
   *
   * @param name        fragmento del nombre
   * @param nationality nacionalidad del autor
   * @return lista de autores filtrados
   */
  List<Author> findByNameContainingIgnoreCaseAndNationality(String name, String nationality);

  /**
   * Obtengo todos los autores ordenados alfabéticamente.
   */
  @Query("SELECT a FROM Author a ORDER BY a.name ASC")
  List<Author> findAllOrderByName();

  /**
   * Obtengo autores que tienen al menos una obra asociada.
   */
  @Query("SELECT DISTINCT a FROM Author a JOIN a.works w")
  List<Author> findAuthorsWithWorks();

  /**
   * Obtengo autores ordenados por número de obras (ranking).
   */
  @Query("""
          SELECT a FROM Author a
          JOIN a.works w
          GROUP BY a
          ORDER BY COUNT(w) DESC
      """)
  List<Author> findTopAuthorsByWorksCount();

  /**
   * Cuenta cuántas obras tiene un autor.
   */
  @Query("""
          SELECT COUNT(w) FROM Author a
          JOIN a.works w
          WHERE a.id = :authorId
      """)
  Long countWorksByAuthorId(Long authorId);
}
