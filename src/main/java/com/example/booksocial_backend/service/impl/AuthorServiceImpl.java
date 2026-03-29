package com.example.booksocial_backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.catalog.AuthorRequestDTO;
import com.example.booksocial_backend.DTO.catalog.AuthorResponseDTO;
import com.example.booksocial_backend.DTO.catalog.WorkResponseDTO;
import com.example.booksocial_backend.domain.catalog.Author;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.exception.AuthorAlreadyExistsException;
import com.example.booksocial_backend.repository.AuthorRepository;
import com.example.booksocial_backend.service.AuthorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository authorRepository;

  @Override
  public AuthorResponseDTO createAuthor(AuthorRequestDTO request) {

    validateAuthorRequest(request);

    String normalizedName = request.getName().trim();

    if (authorRepository.existsByName(normalizedName)) {
      throw new AuthorAlreadyExistsException(normalizedName);
    }

    Author author = Author.builder()
        .name(normalizedName)
        .nationality(request.getNationality())
        .birthDate(request.getBirthDate())
        .build();

    return mapAuthorToDTO(authorRepository.save(author));
  }

  @Override
  @Transactional(readOnly = true)
  public AuthorResponseDTO getAuthorById(Long id) {
    return mapAuthorToDTO(getAuthorEntityById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> getAllAuthors() {
    return authorRepository.findAll()
        .stream()
        .map(this::mapAuthorToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> searchAuthorsByName(String name) {
    return authorRepository.findByNameContainingIgnoreCase(name)
        .stream()
        .map(this::mapAuthorToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> getAuthorsOrderedByName() {
    return authorRepository.findAllOrderByName()
        .stream()
        .map(this::mapAuthorToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> getAuthorsWithWorks() {
    return authorRepository.findAuthorsWithWorks()
        .stream()
        .map(this::mapAuthorToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> getTopAuthors() {
    return authorRepository.findTopAuthorsByWorksCount()
        .stream()
        .map(this::mapAuthorToDTO)
        .toList();
  }

  @Override
  public AuthorResponseDTO updateAuthor(Long id, AuthorRequestDTO request) {

    Author existing = getAuthorEntityById(id);

    String normalizedName = request.getName().trim();

    if (!existing.getName().equalsIgnoreCase(normalizedName)
        && authorRepository.existsByName(normalizedName)) {
      throw new IllegalArgumentException("Ya existe otro autor con ese nombre");
    }

    existing.setName(normalizedName);
    existing.setNationality(request.getNationality());
    existing.setBirthDate(request.getBirthDate());

    return mapAuthorToDTO(authorRepository.save(existing));
  }

  @Override
  public void deleteAuthor(Long id) {
    authorRepository.delete(getAuthorEntityById(id));
  }

  // =========================
  // MAPPERS
  // =========================

  private AuthorResponseDTO mapAuthorToDTO(Author author) {

    return new AuthorResponseDTO(
        author.getId(),
        author.getName(),
        author.getNationality(),
        author.getBirthDate(),
        author.getWorks().stream()
            .map(this::mapWorkToDTO)
            .toList());
  }

  private WorkResponseDTO mapWorkToDTO(Work work) {

    return new WorkResponseDTO(
        work.getId(),
        work.getTitle(),
        work.getDescription(),
        work.getGenre(),
        work.getType(),
        work.getDemographic(),
        work.getPublicationDate(),
        work.getImg(),
        work.getAverageRating(),
        work.getAuthors()
            .stream()
            .map(Author::getName)
            .toList());
  }

  // =========================
  // UTILIDADES
  // =========================

  private Author getAuthorEntityById(Long id) {
    return authorRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Autor no encontrado con id: " + id));
  }

  private void validateAuthorRequest(AuthorRequestDTO request) {

    if (request == null) {
      throw new IllegalArgumentException("La request no puede ser nula");
    }

    if (request.getName() == null || request.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre del autor es obligatorio");
    }
  }
}