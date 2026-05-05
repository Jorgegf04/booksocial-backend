package com.example.booksocial_backend.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.catalog.Tome;
import com.example.booksocial_backend.DTO.catalog.TomeRequestDTO;
import com.example.booksocial_backend.DTO.catalog.TomeResponseDTO;
import com.example.booksocial_backend.domain.catalog.Edition;
import com.example.booksocial_backend.repository.EditionRepository;
import com.example.booksocial_backend.repository.TomeRepository;
import com.example.booksocial_backend.exception.TomeNotFoundException;
import com.example.booksocial_backend.service.TomeService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link TomeService}.
 *
 * Gestiona la lógica de negocio relacionada con los tomos,
 * garantizando la correcta organización de capítulos dentro
 * de una edición de manga.
 *
 * Evita duplicados y permite recuperar tomos ordenados.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TomeServiceImpl implements TomeService {

  private final TomeRepository tomeRepository;
  private final EditionRepository editionRepository;
  private final ModelMapper modelMapper;

  @Override
  public TomeResponseDTO createTome(TomeRequestDTO request) {

    Tome tome = modelMapper.map(request, Tome.class);

    // Set manual relación
    tome.setEdition(Edition.builder().id(request.getEditionId()).build());

    validateTome(tome);

    List<Tome> existing = tomeRepository.findByEditionId(request.getEditionId());

    boolean exists = existing.stream()
        .anyMatch(t -> t.getNumberTome().equals(request.getNumberTome()));

    if (exists) {
      throw new IllegalArgumentException("Ya existe un tomo con ese número en la edición");
    }

    Tome saved = tomeRepository.save(tome);
    updateEditionTotalTomes(saved.getEdition().getId());
    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public TomeResponseDTO getTomeById(Long id) {

    Tome tome = getTomeEntityById(id);

    return mapToDTO(tome);
  }

  @Override
  @Transactional(readOnly = true)
  public List<TomeResponseDTO> getAllTomes() {

    return tomeRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<TomeResponseDTO> getTomesByEdition(Long editionId) {

    return tomeRepository.findByEditionId(editionId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<TomeResponseDTO> getTomesByEditionOrdered(Long editionId) {

    return tomeRepository.findByEditionIdOrderByNumberTomeAsc(editionId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public TomeResponseDTO updateTome(Long id, TomeRequestDTO request) {

    Tome existing = getTomeEntityById(id);

    Long oldEditionId = existing.getEdition().getId(); // 🔥 guardar anterior

    Tome updated = modelMapper.map(request, Tome.class);

    updated.setEdition(Edition.builder().id(request.getEditionId()).build());

    validateTome(updated);

    if (!existing.getNumberTome().equals(updated.getNumberTome())) {

      List<Tome> tomes = tomeRepository.findByEditionId(request.getEditionId());

      boolean exists = tomes.stream()
          .anyMatch(t -> t.getNumberTome().equals(request.getNumberTome()));

      if (exists) {
        throw new IllegalArgumentException("Ya existe otro tomo con ese número");
      }
    }

    existing.setNumberTome(updated.getNumberTome());
    existing.setEdition(updated.getEdition());

    Tome saved = tomeRepository.save(existing);

    updateEditionTotalTomes(oldEditionId);
    updateEditionTotalTomes(saved.getEdition().getId());

    return mapToDTO(saved);
  }

  @Override
  public void deleteTome(Long id) {

    Tome tome = getTomeEntityById(id);

    Long editionId = tome.getEdition().getId();

    tomeRepository.delete(tome);

    updateEditionTotalTomes(editionId);
  }

  private void updateEditionTotalTomes(Long editionId) {

    Edition edition = editionRepository.findById(editionId)
        .orElseThrow(() -> new IllegalArgumentException("Edición no encontrada con id: " + editionId));

    int total = tomeRepository.findByEditionId(editionId).size();

    edition.setTotalTomes(total);

    editionRepository.save(edition);
  }

  /**
   * Convierte Tome → TomeDTO.
   */
  private TomeResponseDTO mapToDTO(Tome tome) {

    return new TomeResponseDTO(
        tome.getId(),
        tome.getNumberTome(),
        tome.getTitle(),

        tome.getEdition() != null ? tome.getEdition().getId() : null,
        tome.getEdition() != null ? tome.getEdition().getTitle() : null,

        (tome.getEdition() != null && tome.getEdition().getWork() != null)
            ? tome.getEdition().getWork().getTitle()
            : null);
  }

  /**
   * Obtiene la entidad Tome o lanza excepción si no existe.
   */
  private Tome getTomeEntityById(Long id) {

    return tomeRepository.findById(id)
        .orElseThrow(() -> new TomeNotFoundException("Tomo no encontrado con id: " + id));
  }

  /**
   * Valida los datos de un tomo.
   */
  private void validateTome(Tome tome) {

    if (tome == null) {
      throw new IllegalArgumentException("El tomo no puede ser nulo");
    }

    if (tome.getNumberTome() == null || tome.getNumberTome() <= 0) {
      throw new IllegalArgumentException("El número de tomo debe ser positivo");
    }

    if (tome.getEdition() == null || tome.getEdition().getId() == null) {
      throw new IllegalArgumentException("El tomo debe pertenecer a una edición válida");
    }
  }

}