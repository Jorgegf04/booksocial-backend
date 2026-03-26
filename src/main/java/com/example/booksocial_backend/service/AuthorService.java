package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.catalog.AuthorRequestDTO;
import com.example.booksocial_backend.DTO.catalog.AuthorResponseDTO;
import com.example.booksocial_backend.domain.catalog.Author;

/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con los
 * autores
 * dentro del sistema BookSocial.
 *
 * Este servicio forma parte del módulo de catálogo y permite realizar
 * operaciones
 * de creación, consulta, actualización y eliminación de autores, así como
 * funcionalidades avanzadas de búsqueda y ranking.
 *
 * Se utiliza como capa intermedia entre los controladores REST y los
 * repositorios,
 * garantizando la validación de datos y la coherencia del dominio.
 *
 * @author Jorge
 * @since 12/03/2026
 * @version 2.0
 */
public interface AuthorService {

  AuthorResponseDTO createAuthor(AuthorRequestDTO request);

  AuthorResponseDTO getAuthorById(Long id);

  List<AuthorResponseDTO> getAllAuthors();

  List<AuthorResponseDTO> searchAuthorsByName(String name);

  List<AuthorResponseDTO> getAuthorsOrderedByName();

  List<AuthorResponseDTO> getAuthorsWithWorks();

  List<AuthorResponseDTO> getTopAuthors();

  AuthorResponseDTO updateAuthor(Long id, AuthorRequestDTO request);

  void deleteAuthor(Long id);

}