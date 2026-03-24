package com.example.booksocial_backend.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.catalog.Tome;
import com.example.booksocial_backend.DTO.catalog.CreateTomeRequest;
import com.example.booksocial_backend.DTO.catalog.TomeDTO;
import com.example.booksocial_backend.domain.catalog.Edition;

import com.example.booksocial_backend.repository.TomeRepository;
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
  private final ModelMapper modelMapper;

  @Override
  public TomeDTO createTome(CreateTomeRequest request) {

    Tome tome = modelMapper.map(request, Tome.class);

    // Set manual relación
    tome.setEdition(Edition.builder().id(request.editionId()).build());

    validateTome(tome);

    List<Tome> existing = tomeRepository.findByEditionId(request.editionId());

    boolean exists = existing.stream()
        .anyMatch(t -> t.getNumberTome().equals(request.numberTome()));

    if (exists) {
      throw new IllegalArgumentException("Ya existe un tomo con ese número en la edición");
    }

    Tome saved = tomeRepository.save(tome);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public TomeDTO getTomeById(Long id) {

    Tome tome = getTomeEntityById(id);

    return mapToDTO(tome);
  }

  @Override
  @Transactional(readOnly = true)
  public List<TomeDTO> getAllTomes() {

    return tomeRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<TomeDTO> getTomesByEdition(Long editionId) {

    return tomeRepository.findByEditionId(editionId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<TomeDTO> getTomesByEditionOrdered(Long editionId) {

    return tomeRepository.findByEditionIdOrderByNumberTomeAsc(editionId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public TomeDTO updateTome(Long id, CreateTomeRequest request) {

    Tome existing = getTomeEntityById(id);

    Tome updated = modelMapper.map(request, Tome.class);

    updated.setEdition(Edition.builder().id(request.editionId()).build());

    validateTome(updated);

    if (!existing.getNumberTome().equals(updated.getNumberTome())) {

      List<Tome> tomes = tomeRepository.findByEditionId(request.editionId());

      boolean exists = tomes.stream()
          .anyMatch(t -> t.getNumberTome().equals(request.numberTome()));

      if (exists) {
        throw new IllegalArgumentException("Ya existe otro tomo con ese número");
      }
    }

    existing.setNumberTome(updated.getNumberTome());
    existing.setEdition(updated.getEdition());

    Tome saved = tomeRepository.save(existing);

    return mapToDTO(saved);
  }

  @Override
  public void deleteTome(Long id) {

    Tome tome = getTomeEntityById(id);

    tomeRepository.delete(tome);
  }

  /**
   * Convierte Tome → TomeDTO.
   */
  private TomeDTO mapToDTO(Tome tome) {

    return new TomeDTO(
        tome.getId(),
        tome.getNumberTome(),
        tome.getEdition().getId());
  }

  /**
   * Obtiene la entidad Tome o lanza excepción si no existe.
   */
  private Tome getTomeEntityById(Long id) {

    return tomeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Tomo no encontrado con id: " + id));
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