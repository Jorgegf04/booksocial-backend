package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.catalog.EditorialRequestDTO;
import com.example.booksocial_backend.DTO.catalog.EditorialResponseDTO;

/**
 * Servicio encargado de la gestión de editoriales dentro del catálogo.
 *
 * Las editoriales representan las entidades responsables de publicar
 * ediciones de obras, siendo un elemento clave en la organización
 * del catálogo.
 *
 * Este servicio permite gestionar las editoriales, validar sus datos
 * y proporcionar funcionalidades de búsqueda y filtrado.
 *
 * @author Jorge
 * @since 12/03/2026
 * @version 3.0
 */
public interface EditorialService {

  EditorialResponseDTO createEditorial(EditorialRequestDTO request);

  EditorialResponseDTO getEditorialById(Long id);

  List<EditorialResponseDTO> getAllEditorials();

  List<EditorialResponseDTO> getEditorialsOrdered();

  List<EditorialResponseDTO> searchEditorialsByName(String name);

  List<EditorialResponseDTO> getEditorialsByCountry(String country);

  EditorialResponseDTO updateEditorial(Long id, EditorialRequestDTO request);

  void deleteEditorial(Long id);
}