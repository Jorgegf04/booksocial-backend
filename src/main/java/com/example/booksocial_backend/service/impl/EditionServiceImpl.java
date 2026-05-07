package com.example.booksocial_backend.service.impl;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.catalog.EditionRequestDTO;
import com.example.booksocial_backend.DTO.catalog.EditionResponseDTO;
import com.example.booksocial_backend.domain.catalog.Edition;
import com.example.booksocial_backend.domain.catalog.Editorial;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.exception.EditionAlreadyExistsException;
import com.example.booksocial_backend.exception.EditionHasOrderLinesException;
import com.example.booksocial_backend.exception.EditionNotFoundException;
import com.example.booksocial_backend.exception.EditorialNotFoundException;
import com.example.booksocial_backend.exception.WorkNotFoundException;
import com.example.booksocial_backend.repository.EditionRepository;
import com.example.booksocial_backend.repository.EditorialRepository;
import com.example.booksocial_backend.repository.OrderLineRepository;
import com.example.booksocial_backend.repository.WorkRepository;
import com.example.booksocial_backend.service.EditionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EditionServiceImpl implements EditionService {

  private static final Logger log = LoggerFactory.getLogger(EditionServiceImpl.class);

  private final EditionRepository editionRepository;
  private final WorkRepository workRepository;
  private final EditorialRepository editorialRepository;
  private final OrderLineRepository orderLineRepository;

  @Override
  public EditionResponseDTO createEdition(EditionRequestDTO request) {

    log.info("[EDITION] [CREATE] [START] isbn='{}'", request != null ? request.getIsbn() : "null");

    if (request == null) {
      throw new IllegalArgumentException("La request no puede ser nula");
    }
    if (request.getIsbn() == null || request.getIsbn().trim().isEmpty()) {
      throw new IllegalArgumentException("El ISBN es obligatorio");
    }
    if (request.getWorkId() == null) {
      throw new IllegalArgumentException("La edición debe estar asociada a una obra");
    }
    if (request.getEditorialId() == null) {
      throw new IllegalArgumentException("La edición debe estar asociada a una editorial");
    }

    Work work = workRepository.findById(request.getWorkId())
        .orElseThrow(() -> new WorkNotFoundException("Obra no encontrada con id: " + request.getWorkId()));

    Editorial editorial = editorialRepository.findById(request.getEditorialId())
        .orElseThrow(() -> new EditorialNotFoundException("Editorial no encontrada con id: " + request.getEditorialId()));

    Edition edition = new Edition();
    edition.setIsbn(request.getIsbn().trim());
    edition.setEditionDate(request.getEditionDate());
    edition.setTitle(request.getTitle());
    edition.setTotalTomes(request.getTotalTomes());
    edition.setWork(work);
    edition.setEditorial(editorial);

    if (editionRepository.findByIsbn(edition.getIsbn()).isPresent()) {
      log.warn("[EDITION] [CREATE] [CONFLICT] Ya existe edición con isbn='{}'", edition.getIsbn());
      throw new EditionAlreadyExistsException(edition.getIsbn());
    }

    Edition saved = Objects.requireNonNull(editionRepository.save(edition));
    log.info("[EDITION] [CREATE] [SUCCESS] id={} isbn='{}'", saved.getId(), saved.getIsbn());
    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public EditionResponseDTO getEditionById(Long id) {
    return mapToDTO(getEditionEntityById(id));
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
        .orElseThrow(() -> new EditionNotFoundException("Edición no encontrada con ISBN: " + isbn));
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

    log.info("[EDITION] [UPDATE] [START] id={}", id);
    Edition existing = getEditionEntityById(id);

    if (request.getIsbn() != null && !request.getIsbn().isBlank()) {
      String normalizedIsbn = request.getIsbn().trim();
      if (!existing.getIsbn().equalsIgnoreCase(normalizedIsbn)
          && editionRepository.findByIsbn(normalizedIsbn).isPresent()) {
        throw new EditionAlreadyExistsException(normalizedIsbn);
      }
      existing.setIsbn(normalizedIsbn);
    }

    if (request.getEditionDate() != null) existing.setEditionDate(request.getEditionDate());
    if (request.getTitle() != null) existing.setTitle(request.getTitle());
    if (request.getTotalTomes() != null) existing.setTotalTomes(request.getTotalTomes());

    if (request.getWorkId() != null) {
      Work work = workRepository.findById(request.getWorkId())
          .orElseThrow(() -> new WorkNotFoundException("Obra no encontrada con id: " + request.getWorkId()));
      existing.setWork(work);
    }

    if (request.getEditorialId() != null) {
      Editorial editorial = editorialRepository.findById(request.getEditorialId())
          .orElseThrow(() -> new EditorialNotFoundException("Editorial no encontrada con id: " + request.getEditorialId()));
      existing.setEditorial(editorial);
    }

    Edition saved = Objects.requireNonNull(editionRepository.save(existing));
    log.info("[EDITION] [UPDATE] [SUCCESS] id={} isbn='{}'", saved.getId(), saved.getIsbn());
    return mapToDTO(saved);
  }

  @Override
  public void deleteEdition(Long id) {
    log.info("[EDITION] [DELETE] [START] id={}", id);
    Edition edition = getEditionEntityById(id);
    boolean hasOrderLines = edition.getProducts() != null &&
        edition.getProducts().stream().anyMatch(p -> orderLineRepository.existsByProductId(p.getId()));
    if (hasOrderLines) {
      log.warn("[EDITION] [DELETE] [CONFLICT] edición id={} tiene pedidos históricos", id);
      throw new EditionHasOrderLinesException(id);
    }
    editionRepository.delete(edition);
    log.info("[EDITION] [DELETE] [SUCCESS] id={} isbn='{}'", id, edition.getIsbn());
  }

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

  private Edition getEditionEntityById(Long id) {
    return editionRepository.findById(id)
        .orElseThrow(() -> new EditionNotFoundException("Edición no encontrada con id: " + id));
  }
}
