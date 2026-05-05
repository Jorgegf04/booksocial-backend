package com.example.booksocial_backend.service.impl;

import java.util.List;

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
    edition.setIsbn(request.getIsbn());
    edition.setEditionDate(request.getEditionDate());
    edition.setTitle(request.getTitle());
    edition.setTotalTomes(request.getTotalTomes());

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

    if (request.getIsbn() == null || request.getIsbn().trim().isEmpty()) {
      throw new IllegalArgumentException("El ISBN es obligatorio");
    }
    if (request.getWorkId() == null) {
      throw new IllegalArgumentException("La edición debe estar asociada a una obra válida");
    }
    if (request.getEditorialId() == null) {
      throw new IllegalArgumentException("La edición debe estar asociada a una editorial válida");
    }

    String normalizedIsbn = request.getIsbn().trim();

    if (!existing.getIsbn().equalsIgnoreCase(normalizedIsbn)
        && editionRepository.findByIsbn(normalizedIsbn).isPresent()) {
      throw new IllegalArgumentException("Ya existe otra edición con ese ISBN");
    }

    Work work = workRepository.findById(request.getWorkId())
        .orElseThrow(() -> new RuntimeException("Work no encontrada con id: " + request.getWorkId()));

    Editorial editorial = editorialRepository.findById(request.getEditorialId())
        .orElseThrow(() -> new RuntimeException("Editorial no encontrada con id: " + request.getEditorialId()));

    existing.setIsbn(normalizedIsbn);
    existing.setEditionDate(request.getEditionDate());
    existing.setTitle(request.getTitle());
    existing.setTotalTomes(request.getTotalTomes());
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

    Integer totalTomes = edition.getTotalTomes() != null
        ? edition.getTotalTomes()
        : (edition.getTomes() != null ? edition.getTomes().size() : 0);

    return new EditionResponseDTO(
        edition.getId(),
        edition.getIsbn(),
        edition.getEditionDate(),

        edition.getTitle(),
        totalTomes,

        edition.getWork() != null ? edition.getWork().getId() : null,
        edition.getWork() != null ? edition.getWork().getTitle() : null,

        edition.getEditorial() != null ? edition.getEditorial().getId() : null,
        edition.getEditorial() != null ? edition.getEditorial().getName() : null);
  }

  /**
   * Obtiene la entidad Edition o lanza excepción si no existe.
   */
  private Edition getEditionEntityById(Long id) {

    return editionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Edición no encontrada con id: " + id));
  }

}