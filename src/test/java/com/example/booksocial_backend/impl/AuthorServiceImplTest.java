package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.booksocial_backend.DTO.catalog.AuthorRequestDTO;
import com.example.booksocial_backend.domain.catalog.Author;
import com.example.booksocial_backend.repository.AuthorRepository;
import com.example.booksocial_backend.service.impl.AuthorServiceImpl;
import com.example.booksocial_backend.exception.AuthorAlreadyExistsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AuthorServiceImplTest {

  @Mock
  private AuthorRepository authorRepository;

  @InjectMocks
  private AuthorServiceImpl authorService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // =========================
  // CREATE AUTHOR
  // =========================

  @Test
  void shouldCreateAuthorSuccessfully() {
    AuthorRequestDTO request = new AuthorRequestDTO(
        "Tolkien",
        "UK",
        LocalDate.of(1892, 1, 3));

    when(authorRepository.existsByName("Tolkien")).thenReturn(false);

    Author savedAuthor = Author.builder()
        .id(1L)
        .name("Tolkien")
        .nationality("UK")
        .birthDate(request.getBirthDate())
        .works(List.of())
        .build();

    when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

    var result = authorService.createAuthor(request);

    assertNotNull(result);
    assertEquals("Tolkien", result.getName());
  }

  @Test
  void shouldThrowExceptionWhenAuthorAlreadyExists() {
    AuthorRequestDTO request = new AuthorRequestDTO(
        "Tolkien",
        "UK",
        LocalDate.now());

    when(authorRepository.existsByName("Tolkien")).thenReturn(true);

    assertThrows(AuthorAlreadyExistsException.class, () -> {
      authorService.createAuthor(request);
    });
  }

  @Test
  void shouldThrowExceptionWhenRequestIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      authorService.createAuthor(null);
    });
  }

  @Test
  void shouldThrowExceptionWhenNameIsEmpty() {
    AuthorRequestDTO request = new AuthorRequestDTO(
        "   ",
        "UK",
        LocalDate.now());

    assertThrows(IllegalArgumentException.class, () -> {
      authorService.createAuthor(request);
    });
  }

  // =========================
  // GET AUTHOR
  // =========================

  @Test
  void shouldGetAuthorById() {
    Author author = Author.builder()
        .id(1L)
        .name("Tolkien")
        .works(List.of())
        .build();

    when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

    var result = authorService.getAuthorById(1L);

    assertEquals("Tolkien", result.getName());
  }

  @Test
  void shouldThrowExceptionWhenAuthorNotFound() {
    when(authorRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      authorService.getAuthorById(1L);
    });
  }

  // =========================
  // GET ALL
  // =========================

  @Test
  void shouldReturnAllAuthors() {
    Author author = Author.builder()
        .id(1L)
        .name("Tolkien")
        .works(List.of())
        .build();

    when(authorRepository.findAll()).thenReturn(List.of(author));

    var result = authorService.getAllAuthors();

    assertEquals(1, result.size());
  }

  // =========================
  // UPDATE AUTHOR
  // =========================

  @Test
  void shouldUpdateAuthorSuccessfully() {
    Author existing = Author.builder()
        .id(1L)
        .name("Old Name")
        .works(List.of())
        .build();

    AuthorRequestDTO request = new AuthorRequestDTO(
        "New Name",
        "Spain",
        LocalDate.now());

    when(authorRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(authorRepository.existsByName("New Name")).thenReturn(false);
    when(authorRepository.save(existing)).thenReturn(existing);

    var result = authorService.updateAuthor(1L, request);

    assertEquals("New Name", result.getName());
  }

  @Test
  void shouldThrowExceptionWhenUpdatingWithExistingName() {
    Author existing = Author.builder()
        .id(1L)
        .name("Old Name")
        .works(List.of())
        .build();

    AuthorRequestDTO request = new AuthorRequestDTO(
        "Existing Name",
        "Spain",
        LocalDate.now());

    when(authorRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(authorRepository.existsByName("Existing Name")).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> {
      authorService.updateAuthor(1L, request);
    });
  }

  // =========================
  // DELETE
  // =========================

  @Test
  void shouldDeleteAuthor() {
    Author author = Author.builder()
        .id(1L)
        .name("Tolkien")
        .works(List.of())
        .build();

    when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

    authorService.deleteAuthor(1L);

    verify(authorRepository).delete(author);
  }
}