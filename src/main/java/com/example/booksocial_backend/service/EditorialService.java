package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.catalog.CreateEditorialRequest;
import com.example.booksocial_backend.DTO.catalog.EditorialDTO;

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

  EditorialDTO createEditorial(CreateEditorialRequest request);

  EditorialDTO getEditorialById(Long id);

  List<EditorialDTO> getAllEditorials();

  List<EditorialDTO> getEditorialsOrdered();

  List<EditorialDTO> searchEditorialsByName(String name);

  List<EditorialDTO> getEditorialsByCountry(String country);

  EditorialDTO updateEditorial(Long id, CreateEditorialRequest request);

  void deleteEditorial(Long id);
}