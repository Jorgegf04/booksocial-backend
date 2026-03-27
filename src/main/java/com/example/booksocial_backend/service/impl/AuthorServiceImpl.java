package com.example.booksocial_backend.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.booksocial_backend.DTO.catalog.AuthorRequestDTO;
import com.example.booksocial_backend.DTO.catalog.AuthorResponseDTO;
import com.example.booksocial_backend.domain.catalog.Author;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.exception.AuthorAlreadyExistsException;
import com.example.booksocial_backend.repository.AuthorRepository;
import com.example.booksocial_backend.service.AuthorService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio de autores.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository authorRepository;
  private final ModelMapper modelMapper;

  @Override
  public AuthorResponseDTO createAuthor(AuthorRequestDTO request) {

    Author author = Author.builder()
        .name(request.getName())
        .birthDate(request.getBirthDate())
        .build();

    validateAuthor(author);

    String normalizedName = author.getName().trim();

    if (authorRepository.existsByName(normalizedName)) {
      throw new AuthorAlreadyExistsException(normalizedName);
    }

    author.setName(normalizedName);

    Author saved = authorRepository.save(author);

    return new AuthorResponseDTO(
        saved.getId(),
        saved.getName(),
        saved.getBirthDate(),
        saved.getWorks()
            .stream()
            .map(work -> work.getTitle())
            .toList());
  }

  @Override
  @Transactional(readOnly = true)
  public AuthorResponseDTO getAuthorById(Long id) {

    Author author = getAuthorEntityById(id);

    return modelMapper.map(author, AuthorResponseDTO.class);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> getAllAuthors() {

    return authorRepository.findAll()
        .stream()
        .map(author -> modelMapper.map(author, AuthorResponseDTO.class))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> searchAuthorsByName(String name) {

    return authorRepository.findByNameContainingIgnoreCase(name)
        .stream()
        .map(author -> modelMapper.map(author, AuthorResponseDTO.class))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> getAuthorsOrderedByName() {

    return authorRepository.findAllOrderByName()
        .stream()
        .map(author -> modelMapper.map(author, AuthorResponseDTO.class))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> getAuthorsWithWorks() {

    return authorRepository.findAuthorsWithWorks()
        .stream()
        .map(author -> new AuthorResponseDTO(
            author.getId(),
            author.getName(),
            author.getBirthDate(),
            author.getWorks()
                .stream()
                .map(work -> work.getTitle())
                .toList()))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> getTopAuthors() {

    return authorRepository.findTopAuthorsByWorksCount()
        .stream()
        .map(author -> new AuthorResponseDTO(
            author.getId(),
            author.getName(),
            author.getBirthDate(),
            author.getWorks()
                .stream()
                .map(Work::getTitle)
                .toList()))
        .toList();
  }

  @Override
  public AuthorResponseDTO updateAuthor(Long id, AuthorRequestDTO request) {

    Author existingAuthor = getAuthorEntityById(id);

    Author updatedData = modelMapper.map(request, Author.class);

    validateAuthor(updatedData);

    String normalizedName = updatedData.getName().trim();

    if (!existingAuthor.getName().equalsIgnoreCase(normalizedName)
        && authorRepository.existsByName(normalizedName)) {
      throw new IllegalArgumentException("Ya existe otro autor con ese nombre");
    }

    existingAuthor.setName(normalizedName);
    existingAuthor.setBirthDate(updatedData.getBirthDate());

    Author saved = authorRepository.save(existingAuthor);

    return modelMapper.map(saved, AuthorResponseDTO.class);
  }

  @Override
  public void deleteAuthor(Long id) {

    Author author = getAuthorEntityById(id);

    authorRepository.delete(author);
  }

  /**
   * Obtiene la entidad Author o lanza excepción si no existe.
   */
  private Author getAuthorEntityById(Long id) {

    return authorRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Autor no encontrado con id: " + id));
  }

  /**
   * Validaciones básicas del autor.
   */
  private void validateAuthor(Author author) {

    if (author == null) {
      throw new IllegalArgumentException("El autor no puede ser nulo");
    }

    if (author.getName() == null || author.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre del autor es obligatorio");
    }
  }
}