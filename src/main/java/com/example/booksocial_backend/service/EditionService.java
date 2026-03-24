package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.catalog.CreateEditionRequest;
import com.example.booksocial_backend.DTO.catalog.EditionDTO;

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

  EditionDTO createEdition(CreateEditionRequest request);

  EditionDTO getEditionById(Long id);

  List<EditionDTO> getAllEditions();

  EditionDTO getEditionByIsbn(String isbn);

  List<EditionDTO> getEditionsByEditorial(Long editorialId);

  EditionDTO updateEdition(Long id, CreateEditionRequest request);

  void deleteEdition(Long id);
}