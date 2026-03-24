package com.example.booksocial_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.catalog.Work;

/**
 * Repositorio de acceso a datos para la entidad {@link Work}.
 *
 * Gestiono la persistencia de obras dentro del sistema BookSocial,
 * proporcionando métodos de búsqueda, filtrado y consulta avanzada
 * del catálogo.
 *
 * Incluyo soporte para:
 * - Búsquedas parciales
 * - Filtros combinados
 * - Paginación
 * - Rankings por valoración
 *
 * Este repositorio es clave para la funcionalidad del catálogo.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 1.1
 */
@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

    /**
     * Busco obras por título (búsqueda parcial).
     */
    List<Work> findByTitleContainingIgnoreCase(String title);

    /**
     * Busco obras por género.
     */
    List<Work> findByGenreIgnoreCase(String genre);

    /**
     * Busco obras publicadas después de una fecha.
     */
    List<Work> findByPublicationDateAfter(LocalDate date);

    /**
     * Busco obras con valoración mayor o igual a la indicada.
     */
    List<Work> findByAverageRatingGreaterThanEqual(Double rating);

    /**
     * Busco obras por nombre de autor.
     */
    List<Work> findByAuthors_NameContainingIgnoreCase(String authorName);

    /**
     * Busco obras por autor exacto.
     */
    @Query("SELECT w FROM Work w JOIN w.authors a WHERE a.id = :authorId")
    List<Work> findByAuthorId(Long authorId);

    /**
     * Busco obras con filtros combinados.
     *
     * Permite construir búsquedas avanzadas en el catálogo.
     */
    @Query("""
                SELECT w FROM Work w
                WHERE (:title IS NULL OR LOWER(w.title) LIKE LOWER(CONCAT('%', :title, '%')))
                AND (:genre IS NULL OR LOWER(w.genre) = LOWER(:genre))
                AND (:rating IS NULL OR w.averageRating >= :rating)
            """)
    List<Work> searchWorks(String title, String genre, Double rating);

    /**
     * Obtengo obras ordenadas por valoración descendente.
     */
    List<Work> findAllByOrderByAverageRatingDesc();

    /**
     * Obtengo las mejores obras (top rating).
     */
    @Query("""
                SELECT w FROM Work w
                WHERE w.averageRating IS NOT NULL
                ORDER BY w.averageRating DESC
            """)
    List<Work> findTopRatedWorks();

    /**
     * Obtengo obras paginadas.
     */
    Page<Work> findAll(Pageable pageable);

    /**
     * Busco obras por título con paginación.
     */
    Page<Work> findByTitleContainingIgnoreCase(String title, Pageable pageable);

}
