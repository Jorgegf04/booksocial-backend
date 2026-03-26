package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.catalog.VolumeRequestDTO;
import com.example.booksocial_backend.DTO.catalog.VolumeResponseDTO;

/**
 * Servicio encargado de la gestión de volúmenes dentro del catálogo.
 *
 * Los volúmenes representan la estructura de obras tipo cómic,
 * organizadas dentro de una edición.
 *
 * Este servicio garantiza la coherencia estructural,
 * evitando duplicados y permitiendo la recuperación ordenada.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
public interface VolumeService {

  VolumeResponseDTO createVolume(VolumeRequestDTO request);

  VolumeResponseDTO getVolumeById(Long id);

  List<VolumeResponseDTO> getAllVolumes();

  List<VolumeResponseDTO> getVolumesByEdition(Long editionId);

  List<VolumeResponseDTO> getVolumesByEditionOrdered(Long editionId);

  VolumeResponseDTO updateVolume(Long id, VolumeRequestDTO request);

  void deleteVolume(Long id);
}