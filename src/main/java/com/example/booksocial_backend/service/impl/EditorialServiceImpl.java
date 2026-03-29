package com.example.booksocial_backend.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.catalog.EditorialRequestDTO;
import com.example.booksocial_backend.DTO.catalog.EditorialResponseDTO;
import com.example.booksocial_backend.domain.catalog.Editorial;
import com.example.booksocial_backend.repository.EditorialRepository;
import com.example.booksocial_backend.service.EditorialService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link EditorialService}.
 *
 * Gestiona la lógica de negocio relacionada con las editoriales,
 * incluyendo validaciones, control de duplicados y funcionalidades
 * de filtrado dentro del catálogo.
 *
 * Forma parte del módulo de catálogo, siendo fundamental para la
 * organización de las ediciones de obras.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 4.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EditorialServiceImpl implements EditorialService {

  private final EditorialRepository editorialRepository;
  private final ModelMapper modelMapper;

  @Override
  public EditorialResponseDTO createEditorial(EditorialRequestDTO request) {

    Editorial editorial = modelMapper.map(request, Editorial.class);

    validateEditorial(editorial);

    String normalizedName = editorial.getName().trim();

    if (editorialRepository.existsByName(normalizedName)) {
      throw new IllegalArgumentException("Ya existe una editorial con ese nombre");
    }

    editorial.setName(normalizedName);

    Editorial saved = editorialRepository.save(editorial);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public EditorialResponseDTO getEditorialById(Long id) {

    Editorial editorial = getEditorialEntityById(id);

    return mapToDTO(editorial);
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

    Editorial saved = editorialRepository.save(existing);

    return mapToDTO(saved);
  }

  @Override
  public void deleteEditorial(Long id) {

    Editorial editorial = getEditorialEntityById(id);

    editorialRepository.delete(editorial);
  }

  /**
   * Convierte Editorial → EditorialResponseDTO.
   *
   * Incluye información enriquecida como el número total de ediciones.
   */
  private EditorialResponseDTO mapToDTO(Editorial editorial) {

    return new EditorialResponseDTO(
        editorial.getId(),
        editorial.getName(),
        editorial.getCountry(),
        editorial.getEditions() != null ? editorial.getEditions().size() : 0);
  }

  /**
   * Obtiene la entidad Editorial o lanza excepción si no existe.
   */
  private Editorial getEditorialEntityById(Long id) {

    return editorialRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Editorial no encontrada con id: " + id));
  }

  /**
   * Valida los datos de una editorial.
   *
   * @param editorial editorial a validar
   */
  private void validateEditorial(Editorial editorial) {

    if (editorial == null) {
      throw new IllegalArgumentException("La editorial no puede ser nula");
    }

    if (editorial.getName() == null || editorial.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre de la editorial es obligatorio");
    }
  }
}