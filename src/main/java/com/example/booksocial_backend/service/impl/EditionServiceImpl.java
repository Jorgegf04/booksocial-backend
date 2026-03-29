package com.example.booksocial_backend.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.catalog.EditionRequestDTO;
import com.example.booksocial_backend.DTO.catalog.EditionResponseDTO;
import com.example.booksocial_backend.domain.catalog.Edition;
import com.example.booksocial_backend.domain.catalog.Editorial;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.repository.EditionRepository;
import com.example.booksocial_backend.repository.EditorialRepository;
import com.example.booksocial_backend.repository.WorkRepository;
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
  private final WorkRepository workRepository;
  private final EditorialRepository editorialRepository;
  private final ModelMapper modelMapper;

  @Override
  public EditionResponseDTO createEdition(EditionRequestDTO request) {

    Edition edition = new Edition();

    edition.setIsbn(request.getIsbn());
    edition.setEditionDate(request.getEditionDate());

    Work work = workRepository.findById(request.getWorkId())
        .orElseThrow(() -> new RuntimeException("Work no encontrado"));

    Editorial editorial = editorialRepository.findById(request.getEditorialId())
        .orElseThrow(() -> new RuntimeException("Editorial no encontrada"));

    edition.setWork(work);
    edition.setEditorial(editorial);

    Edition saved = editionRepository.save(edition);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public EditionResponseDTO getEditionById(Long id) {

    Edition edition = getEditionEntityById(id);

    return mapToDTO(edition);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EditionResponseDTO> getAllEditions() {

    return editionRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public EditionResponseDTO getEditionByIsbn(String isbn) {

    Edition edition = editionRepository.findByIsbn(isbn)
        .orElseThrow(() -> new RuntimeException("Edición no encontrada con ISBN: " + isbn));

    return mapToDTO(edition);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EditionResponseDTO> getEditionsByEditorial(Long editorialId) {

    return editionRepository.findByEditorialId(editorialId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public EditionResponseDTO updateEdition(Long id, EditionRequestDTO request) {

    Edition existing = getEditionEntityById(id);

    Edition updated = modelMapper.map(request, Edition.class);

    Work work = workRepository.findById(request.getWorkId())
        .orElseThrow(() -> new RuntimeException("Work no encontrada con id: " + request.getWorkId()));

    Editorial editorial = editorialRepository.findById(request.getEditorialId())
        .orElseThrow(() -> new RuntimeException("Editorial no encontrada con id: " + request.getEditorialId()));

    validateEdition(updated);

    String normalizedIsbn = updated.getIsbn().trim();

    if (!existing.getIsbn().equalsIgnoreCase(normalizedIsbn)
        && editionRepository.findByIsbn(normalizedIsbn).isPresent()) {
      throw new IllegalArgumentException("Ya existe otra edición con ese ISBN");
    }

    existing.setIsbn(normalizedIsbn);
    existing.setEditionDate(updated.getEditionDate());
    existing.setWork(work);
    existing.setEditorial(editorial);

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
  private EditionResponseDTO mapToDTO(Edition edition) {
    return new EditionResponseDTO(
        edition.getId(),
        edition.getIsbn(),
        edition.getEditionDate(),

        edition.getWork() != null ? edition.getWork().getId() : null,
        edition.getWork() != null ? edition.getWork().getTitle() : null,

        edition.getEditorial() != null ? edition.getEditorial().getId() : null,
        edition.getEditorial() != null ? edition.getEditorial().getName() : null,

        edition.getTotalTomes());
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