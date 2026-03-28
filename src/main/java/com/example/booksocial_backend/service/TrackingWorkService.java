package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.social.*;

/**
 * Servicio encargado de la gestión del seguimiento de obras.
 *
 * Proporciona operaciones CRUD y consultas sobre las relaciones
 * entre usuarios y obras dentro del sistema social.
 *
 * @author Jorge
 * @since 2026
 */
public interface TrackingWorkService {

  TrackingWorkResponseDTO create(TrackingWorkRequestDTO request);

  List<TrackingWorkResponseDTO> createMany(List<TrackingWorkRequestDTO> requests);

  List<TrackingWorkResponseDTO> getByUser(Long userId);

  List<TrackingWorkResponseDTO> getByWork(Long workId);

  void delete(Long id);
}