package com.example.booksocial_backend.service.impl;

import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.catalog.Volume;
import com.example.booksocial_backend.DTO.catalog.VolumeRequestDTO;
import com.example.booksocial_backend.DTO.catalog.VolumeResponseDTO;
import com.example.booksocial_backend.domain.catalog.Edition;
import com.example.booksocial_backend.exception.EditionNotFoundException;
import com.example.booksocial_backend.exception.VolumeNotFoundException;
import com.example.booksocial_backend.repository.EditionRepository;
import com.example.booksocial_backend.repository.VolumeRepository;
import com.example.booksocial_backend.service.VolumeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VolumeServiceImpl implements VolumeService {

  private static final Logger log = LoggerFactory.getLogger(VolumeServiceImpl.class);

  private final VolumeRepository volumeRepository;
  private final EditionRepository editionRepository;
  private final ModelMapper modelMapper;

  @Override
  public VolumeResponseDTO createVolume(VolumeRequestDTO request) {

    log.info("[VOLUME] [CREATE] [START] editionId={} number={}", request.getEditionId(), request.getVolumeNumber());

    Edition edition = editionRepository.findById(Objects.requireNonNull(request.getEditionId()))
        .orElseThrow(() -> new EditionNotFoundException("Edición no encontrada con id: " + request.getEditionId()));

    Volume volume = modelMapper.map(request, Volume.class);
    volume.setEdition(edition);

    validateVolume(volume);

    boolean exists = volumeRepository.findByEditionId(request.getEditionId()).stream()
        .anyMatch(v -> v.getVolumeNumber().equals(request.getVolumeNumber()));

    if (exists) {
      throw new IllegalArgumentException("Ya existe un volumen con ese número en la edición");
    }

    volume.setTitle(volume.getTitle().trim());

    Volume saved = Objects.requireNonNull(volumeRepository.save(volume));
    log.info("[VOLUME] [CREATE] [SUCCESS] id={} number={}", saved.getId(), saved.getVolumeNumber());
    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public VolumeResponseDTO getVolumeById(Long id) {
    return mapToDTO(getVolumeEntityById(id));
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

    log.info("[VOLUME] [UPDATE] [START] id={}", id);
    Volume existing = getVolumeEntityById(id);

    Edition edition = editionRepository.findById(Objects.requireNonNull(request.getEditionId()))
        .orElseThrow(() -> new EditionNotFoundException("Edición no encontrada con id: " + request.getEditionId()));

    Volume updated = modelMapper.map(request, Volume.class);
    updated.setEdition(edition);
    validateVolume(updated);

    if (!existing.getVolumeNumber().equals(updated.getVolumeNumber())) {
      boolean exists = volumeRepository.findByEditionId(request.getEditionId()).stream()
          .anyMatch(v -> v.getVolumeNumber().equals(request.getVolumeNumber()));
      if (exists) {
        throw new IllegalArgumentException("Ya existe otro volumen con ese número");
      }
    }

    existing.setVolumeNumber(updated.getVolumeNumber());
    existing.setTitle(updated.getTitle().trim());
    existing.setEdition(updated.getEdition());

    Volume saved = Objects.requireNonNull(volumeRepository.save(existing));
    log.info("[VOLUME] [UPDATE] [SUCCESS] id={} number={}", saved.getId(), saved.getVolumeNumber());
    return mapToDTO(saved);
  }

  @Override
  public void deleteVolume(Long id) {
    log.info("[VOLUME] [DELETE] [START] id={}", id);
    Volume volume = getVolumeEntityById(id);
    volumeRepository.delete(Objects.requireNonNull(volume));
    log.info("[VOLUME] [DELETE] [SUCCESS] id={}", id);
  }

  private VolumeResponseDTO mapToDTO(Volume volume) {
    Edition edition = volume.getEdition();
    return new VolumeResponseDTO(
        volume.getId(),
        volume.getVolumeNumber(),
        volume.getTitle(),
        edition != null ? edition.getId() : null,
        edition != null ? edition.getIsbn() : null,
        (edition != null && edition.getWork() != null) ? edition.getWork().getTitle() : null);
  }

  private Volume getVolumeEntityById(Long id) {
    return volumeRepository.findById(id)
        .orElseThrow(() -> new VolumeNotFoundException("Volumen no encontrado con id: " + id));
  }

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
