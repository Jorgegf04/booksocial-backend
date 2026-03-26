package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.catalog.EditionRequestDTO;
import com.example.booksocial_backend.DTO.catalog.EditionResponseDTO;

/**
 * Servicio encargado de la gestión de ediciones dentro del sistema BookSocial.
 *
 * La entidad Edition representa una versión concreta de una obra y actúa como
 * punto de conexión entre el catálogo y el sistema de productos y ventas.
 *
 * Este servicio permite gestionar las ediciones, validar su integridad y
 * proporcionar métodos de consulta utilizados en el catálogo.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 2.1
 */
public interface EditionService {

  EditionResponseDTO createEdition(EditionRequestDTO request);

  EditionResponseDTO getEditionById(Long id);

  List<EditionResponseDTO> getAllEditions();

  EditionResponseDTO getEditionByIsbn(String isbn);

  List<EditionResponseDTO> getEditionsByEditorial(Long editorialId);

  EditionResponseDTO updateEdition(Long id, EditionRequestDTO request);

  void deleteEdition(Long id);
}