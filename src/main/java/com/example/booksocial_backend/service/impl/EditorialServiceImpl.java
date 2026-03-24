package com.example.booksocial_backend.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.catalog.CreateEditorialRequest;
import com.example.booksocial_backend.DTO.catalog.EditorialDTO;
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
 * @version 3.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EditorialServiceImpl implements EditorialService {

  private final EditorialRepository editorialRepository;
  private final ModelMapper modelMapper;

  @Override
  public EditorialDTO createEditorial(CreateEditorialRequest request) {

    Editorial editorial = modelMapper.map(request, Editorial.class);

    validateEditorial(editorial);

    String normalizedName = editorial.getName().trim();

    if (editorialRepository.existsByName(normalizedName)) {
      throw new IllegalArgumentException("Ya existe una editorial con ese nombre");
    }

    editorial.setName(normalizedName);

    Editorial saved = editorialRepository.save(editorial);

    return modelMapper.map(saved, EditorialDTO.class);
  }

  @Override
  @Transactional(readOnly = true)
  public EditorialDTO getEditorialById(Long id) {

    Editorial editorial = getEditorialEntityById(id);

    return modelMapper.map(editorial, EditorialDTO.class);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EditorialDTO> getAllEditorials() {

    return editorialRepository.findAll()
        .stream()
        .map(editorial -> modelMapper.map(editorial, EditorialDTO.class))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<EditorialDTO> getEditorialsOrdered() {

    return editorialRepository.findAllByOrderByNameAsc()
        .stream()
        .map(editorial -> modelMapper.map(editorial, EditorialDTO.class))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<EditorialDTO> searchEditorialsByName(String name) {

    return editorialRepository.findByNameContainingIgnoreCase(name)
        .stream()
        .map(editorial -> modelMapper.map(editorial, EditorialDTO.class))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<EditorialDTO> getEditorialsByCountry(String country) {

    return editorialRepository.findByCountryIgnoreCase(country)
        .stream()
        .map(editorial -> modelMapper.map(editorial, EditorialDTO.class))
        .toList();
  }

  @Override
  public EditorialDTO updateEditorial(Long id, CreateEditorialRequest request) {

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

    return modelMapper.map(saved, EditorialDTO.class);
  }

  @Override
  public void deleteEditorial(Long id) {

    Editorial editorial = getEditorialEntityById(id);

    editorialRepository.delete(editorial);
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