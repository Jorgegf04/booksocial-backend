package com.example.booksocial_backend.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.catalog.CreateEditionRequest;
import com.example.booksocial_backend.DTO.catalog.EditionDTO;
import com.example.booksocial_backend.domain.catalog.Edition;
import com.example.booksocial_backend.domain.catalog.Editorial;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.repository.EditionRepository;
import com.example.booksocial_backend.service.EditionService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link EditionService}.
 *
 * Gestiona la lógica de negocio asociada a las ediciones de obras,
 * garantizando la validez de los datos y la coherencia con el modelo
 * del sistema.
 *
 * Se encarga de validar el ISBN, evitar duplicidades y asegurar que
 * cada edición esté correctamente asociada a una obra y una editorial.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EditionServiceImpl implements EditionService {

  private final EditionRepository editionRepository;
  private final ModelMapper modelMapper;

  @Override
  public EditionDTO createEdition(CreateEditionRequest request) {

    Edition edition = modelMapper.map(request, Edition.class);

    // Set manual de relaciones
    edition.setWork(Work.builder().id(request.workId()).build());
    edition.setEditorial(Editorial.builder().id(request.editorialId()).build());

    validateEdition(edition);

    String normalizedIsbn = edition.getIsbn().trim();

    if (editionRepository.findByIsbn(normalizedIsbn).isPresent()) {
      throw new IllegalArgumentException("Ya existe una edición con ese ISBN");
    }

    edition.setIsbn(normalizedIsbn);

    Edition saved = editionRepository.save(edition);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public EditionDTO getEditionById(Long id) {

    Edition edition = getEditionEntityById(id);

    return mapToDTO(edition);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EditionDTO> getAllEditions() {

    return editionRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public EditionDTO getEditionByIsbn(String isbn) {

    Edition edition = editionRepository.findByIsbn(isbn)
        .orElseThrow(() -> new RuntimeException("Edición no encontrada con ISBN: " + isbn));

    return mapToDTO(edition);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EditionDTO> getEditionsByEditorial(Long editorialId) {

    return editionRepository.findByEditorialId(editorialId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public EditionDTO updateEdition(Long id, CreateEditionRequest request) {

    Edition existing = getEditionEntityById(id);

    Edition updated = modelMapper.map(request, Edition.class);

    updated.setWork(Work.builder().id(request.workId()).build());
    updated.setEditorial(Editorial.builder().id(request.editorialId()).build());

    validateEdition(updated);

    String normalizedIsbn = updated.getIsbn().trim();

    if (!existing.getIsbn().equalsIgnoreCase(normalizedIsbn)
        && editionRepository.findByIsbn(normalizedIsbn).isPresent()) {
      throw new IllegalArgumentException("Ya existe otra edición con ese ISBN");
    }

    existing.setIsbn(normalizedIsbn);
    existing.setEditionDate(updated.getEditionDate());
    existing.setWork(updated.getWork());
    existing.setEditorial(updated.getEditorial());

    Edition saved = editionRepository.save(existing);

    return mapToDTO(saved);
  }

  @Override
  public void deleteEdition(Long id) {

    Edition edition = getEditionEntityById(id);

    editionRepository.delete(edition);
  }

  /**
   * Convierte una entidad Edition en su DTO correspondiente.
   */
  private EditionDTO mapToDTO(Edition edition) {

    return new EditionDTO(
        edition.getId(),
        edition.getIsbn(),
        edition.getEditionDate(),
        edition.getWork().getId(),
        edition.getEditorial().getId());
  }

  /**
   * Obtiene la entidad Edition o lanza excepción si no existe.
   */
  private Edition getEditionEntityById(Long id) {

    return editionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Edición no encontrada con id: " + id));
  }

  /**
   * Valida los datos básicos de una edición.
   *
   * @param edition edición a validar
   */
  private void validateEdition(Edition edition) {

    if (edition == null) {
      throw new IllegalArgumentException("La edición no puede ser nula");
    }

    if (edition.getIsbn() == null || edition.getIsbn().trim().isEmpty()) {
      throw new IllegalArgumentException("El ISBN es obligatorio");
    }

    if (edition.getWork() == null || edition.getWork().getId() == null) {
      throw new IllegalArgumentException("La edición debe estar asociada a una obra válida");
    }

    if (edition.getEditorial() == null || edition.getEditorial().getId() == null) {
      throw new IllegalArgumentException("La edición debe estar asociada a una editorial válida");
    }
  }
}