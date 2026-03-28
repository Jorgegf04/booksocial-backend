package com.example.booksocial_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.social.TrackingWork;

/**
 * Repositorio de acceso a datos para la entidad {@link TrackingWork}.
 *
 * Permite gestionar las relaciones de seguimiento entre usuarios y obras.
 *
 * Incluye métodos para:
 * <ul>
 * <li>Consultar seguimientos por usuario</li>
 * <li>Consultar seguidores de una obra</li>
 * <li>Evitar duplicados</li>
 * </ul>
 *
 * @author Jorge
 * @since 2026
 */
@Repository
public interface TrackingWorkRepository extends JpaRepository<TrackingWork, Long> {

  List<TrackingWork> findByUserId(Long userId);

  List<TrackingWork> findByWorkId(Long workId);

  boolean existsByUserIdAndWorkId(Long userId, Long workId);
}