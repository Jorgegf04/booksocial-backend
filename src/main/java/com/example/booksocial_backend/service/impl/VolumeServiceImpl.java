package com.example.booksocial_backend.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.catalog.Volume;
import com.example.booksocial_backend.DTO.catalog.VolumeRequestDTO;
import com.example.booksocial_backend.DTO.catalog.VolumeResponseDTO;
import com.example.booksocial_backend.domain.catalog.Edition;
import com.example.booksocial_backend.repository.VolumeRepository;
import com.example.booksocial_backend.service.VolumeService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link VolumeService}.
 *
 * Gestiona la lógica de negocio relacionada con los volúmenes,
 * garantizando la correcta organización dentro de una edición.
 *
 * Evita duplicados y permite recuperar volúmenes ordenados.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class VolumeServiceImpl implements VolumeService {

  private final VolumeRepository volumeRepository;
  private final ModelMapper modelMapper;

  @Override
  public VolumeResponseDTO createVolume(VolumeRequestDTO request) {

    Volume volume = modelMapper.map(request, Volume.class);

    // Set manual relación
    volume.setEdition(Edition.builder().id(request.getEditionId()).build());

    validateVolume(volume);

    List<Volume> existing = volumeRepository.findByEditionId(request.getEditionId());

    boolean exists = existing.stream()
        .anyMatch(v -> v.getVolumeNumber().equals(request.getVolumeNumber()));

    if (exists) {
      throw new IllegalArgumentException("Ya existe un volumen con ese número en la edición");
    }

    volume.setTitle(volume.getTitle().trim());

    Volume saved = volumeRepository.save(volume);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public VolumeResponseDTO getVolumeById(Long id) {

    Volume volume = getVolumeEntityById(id);

    return mapToDTO(volume);
  }

  @Override
  @Transactional(readOnly = true)
  public List<VolumeResponseDTO> getAllVolumes() {

    return volumeRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<VolumeResponseDTO> getVolumesByEdition(Long editionId) {

    return volumeRepository.findByEditionId(editionId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<VolumeResponseDTO> getVolumesByEditionOrdered(Long editionId) {

    return volumeRepository.findByEditionIdOrderByVolumeNumberAsc(editionId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public VolumeResponseDTO updateVolume(Long id, VolumeRequestDTO request) {

    Volume existing = getVolumeEntityById(id);

    Volume updated = modelMapper.map(request, Volume.class);

    updated.setEdition(Edition.builder().id(request.getEditionId()).build());

    validateVolume(updated);

    if (!existing.getVolumeNumber().equals(updated.getVolumeNumber())) {

      List<Volume> volumes = volumeRepository.findByEditionId(request.getEditionId());

      boolean exists = volumes.stream()
          .anyMatch(v -> v.getVolumeNumber().equals(request.getVolumeNumber()));

      if (exists) {
        throw new IllegalArgumentException("Ya existe otro volumen con ese número");
      }
    }

    existing.setVolumeNumber(updated.getVolumeNumber());
    existing.setTitle(updated.getTitle().trim());
    existing.setEdition(updated.getEdition());

    Volume saved = volumeRepository.save(existing);

    return mapToDTO(saved);
  }

  @Override
  public void deleteVolume(Long id) {

    Volume volume = getVolumeEntityById(id);

    volumeRepository.delete(volume);
  }

  /**
   * Convierte Volume → VolumeDTO.
   */
  private VolumeResponseDTO mapToDTO(Volume volume) {

    return new VolumeResponseDTO(
        volume.getId(),
        volume.getVolumeNumber(),
        volume.getTitle(),
        volume.getEdition().getId());
  }

  /**
   * Obtiene la entidad Volume o lanza excepción si no existe.
   */
  private Volume getVolumeEntityById(Long id) {

    return volumeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Volumen no encontrado con id: " + id));
  }

  /**
   * Valida los datos de un volumen.
   */
  private void validateVolume(Volume volume) {

    if (volume == null) {
      throw new IllegalArgumentException("El volumen no puede ser nulo");
    }

    if (volume.getVolumeNumber() == null || volume.getVolumeNumber() <= 0) {
      throw new IllegalArgumentException("El número de volumen debe ser positivo");
    }

    if (volume.getTitle() == null || volume.getTitle().trim().isEmpty()) {
      throw new IllegalArgumentException("El título es obligatorio");
    }

    if (volume.getEdition() == null || volume.getEdition().getId() == null) {
      throw new IllegalArgumentException("El volumen debe pertenecer a una edición válida");
    }
  }
}