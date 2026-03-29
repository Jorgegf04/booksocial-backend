package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.social.*;

/**
 * Servicio encargado de la gestión del seguimiento de obras.
 *
 * <p>
 * Define las operaciones de negocio relacionadas con la interacción
 * entre usuarios y obras dentro del sistema social.
 * </p>
 *
 * <ul>
 * <li>Crear seguimiento</li>
 * <li>Crear seguimiento masivo</li>
 * <li>Consultar seguimientos por usuario</li>
 * <li>Consultar seguimientos por obra</li>
 * <li>Eliminar seguimiento</li>
 * </ul>
 *
 * @author Jorge
 * @since 2026
 */
public interface TrackingWorkService {

  /**
   * Crea un seguimiento de obra.
   *
   * @param request DTO de entrada
   * @return seguimiento creado
   */
  TrackingWorkResponseDTO create(TrackingWorkRequestDTO request);

  /**
   * Crea múltiples seguimientos.
   *
   * @param requests lista de solicitudes
   * @return lista de seguimientos
   */
  List<TrackingWorkResponseDTO> createMany(List<TrackingWorkRequestDTO> requests);

  /**
   * Obtiene seguimientos por usuario.
   *
   * @param userId ID del usuario
   * @return lista de seguimientos
   */
  List<TrackingWorkResponseDTO> getByUser(Long userId);

  /**
   * Obtiene seguimientos por obra.
   *
   * @param workId ID de la obra
   * @return lista de seguimientos
   */
  List<TrackingWorkResponseDTO> getByWork(Long workId);

  /**
   * Elimina un seguimiento.
   *
   * @param id ID del seguimiento
   */
  void delete(Long id);

  /**
   * Actualiza el estado de un seguimiento de obra.
   *
   * @param id      identificador del seguimiento
   * @param request DTO con el nuevo estado
   * @return seguimiento actualizado
   */
  TrackingWorkResponseDTO updateStatus(Long id, TrackingWorkRequestDTO request);
}