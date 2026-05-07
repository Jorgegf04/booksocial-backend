package com.example.booksocial_backend.service.impl;

import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.catalog.EditorialRequestDTO;
import com.example.booksocial_backend.DTO.catalog.EditorialResponseDTO;
import com.example.booksocial_backend.domain.catalog.Editorial;
import com.example.booksocial_backend.exception.EditorialAlreadyExistsException;
import com.example.booksocial_backend.exception.EditorialHasEditionsException;
import com.example.booksocial_backend.exception.EditorialNotFoundException;
import com.example.booksocial_backend.repository.EditorialRepository;
import com.example.booksocial_backend.service.EditorialService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EditorialServiceImpl implements EditorialService {

  private static final Logger log = LoggerFactory.getLogger(EditorialServiceImpl.class);

  private final EditorialRepository editorialRepository;
  private final ModelMapper modelMapper;

  @Override
  public EditorialResponseDTO createEditorial(EditorialRequestDTO request) {

    log.info("[EDITORIAL] [CREATE] [START] name='{}'", request != null ? request.getName() : "null");

    Editorial editorial = modelMapper.map(request, Editorial.class);
    validateEditorial(editorial);

    String normalizedName = editorial.getName().trim();
    if (editorialRepository.existsByName(normalizedName)) {
      log.warn("[EDITORIAL] [CREATE] [CONFLICT] Ya existe editorial con nombre='{}'", normalizedName);
      throw new EditorialAlreadyExistsException(normalizedName);
    }

    editorial.setName(normalizedName);
    Editorial saved = Objects.requireNonNull(editorialRepository.save(editorial));
    log.info("[EDITORIAL] [CREATE] [SUCCESS] id={} name='{}'", saved.getId(), saved.getName());
    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public EditorialResponseDTO getEditorialById(Long id) {
    return mapToDTO(getEditorialEntityById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<EditorialResponseDTO> getAllEditorials() {
    return editorialRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<EditorialResponseDTO> getEditorialsOrdered() {
    return editorialRepository.findAllByOrderByNameAsc()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<EditorialResponseDTO> searchEditorialsByName(String name) {
    return editorialRepository.findByNameContainingIgnoreCase(name)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<EditorialResponseDTO> getEditorialsByCountry(String country) {
    return editorialRepository.findByCountryIgnoreCase(country)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public EditorialResponseDTO updateEditorial(Long id, EditorialRequestDTO request) {

    log.info("[EDITORIAL] [UPDATE] [START] id={}", id);
    Editorial existing = getEditorialEntityById(id);
    Editorial updated = modelMapper.map(request, Editorial.class);
    validateEditorial(updated);

    String normalizedName = updated.getName().trim();
    if (!existing.getName().equalsIgnoreCase(normalizedName)
        && editorialRepository.existsByName(normalizedName)) {
      throw new IllegalArgumentException("Ya existe otra editorial con ese nombre");
    }

    existing.setName(normalizedName);
    existing.setCountry(updated.getCountry());

    Editorial saved = Objects.requireNonNull(editorialRepository.save(existing));
    log.info("[EDITORIAL] [UPDATE] [SUCCESS] id={} name='{}'", saved.getId(), saved.getName());
    return mapToDTO(saved);
  }

  @Override
  public void deleteEditorial(Long id) {
    log.info("[EDITORIAL] [DELETE] [START] id={}", id);
    Editorial editorial = getEditorialEntityById(id);
    int editionCount = editorial.getEditions() != null ? editorial.getEditions().size() : 0;
    if (editionCount > 0) {
      log.warn("[EDITORIAL] [DELETE] [CONFLICT] editorial='{}' tiene {} edición(es)", editorial.getName(), editionCount);
      throw new EditorialHasEditionsException(editorial.getName(), editionCount);
    }
    editorialRepository.delete(editorial);
    log.info("[EDITORIAL] [DELETE] [SUCCESS] id={} name='{}'", id, editorial.getName());
  }

  private EditorialResponseDTO mapToDTO(Editorial editorial) {
    return new EditorialResponseDTO(
        editorial.getId(),
        editorial.getName(),
        editorial.getCountry(),
        editorial.getEditions() != null ? editorial.getEditions().size() : 0);
  }

  private Editorial getEditorialEntityById(Long id) {
    return editorialRepository.findById(id)
        .orElseThrow(() -> new EditorialNotFoundException("Editorial no encontrada con id: " + id));
  }

  private void validateEditorial(Editorial editorial) {
    if (editorial == null) {
      throw new IllegalArgumentException("La editorial no puede ser nula");
    }
    if (editorial.getName() == null || editorial.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre de la editorial es obligatorio");
    }
  }
}
