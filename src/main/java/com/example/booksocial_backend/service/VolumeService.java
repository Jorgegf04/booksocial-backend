package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.catalog.CreateVolumeRequest;
import com.example.booksocial_backend.DTO.catalog.VolumeDTO;

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

  VolumeDTO createVolume(CreateVolumeRequest request);

  VolumeDTO getVolumeById(Long id);

  List<VolumeDTO> getAllVolumes();

  List<VolumeDTO> getVolumesByEdition(Long editionId);

  List<VolumeDTO> getVolumesByEditionOrdered(Long editionId);

  VolumeDTO updateVolume(Long id, CreateVolumeRequest request);

  void deleteVolume(Long id);
}