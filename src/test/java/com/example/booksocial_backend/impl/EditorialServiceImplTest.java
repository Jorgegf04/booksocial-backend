package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.example.booksocial_backend.DTO.catalog.EditorialRequestDTO;
import com.example.booksocial_backend.domain.catalog.Editorial;
import com.example.booksocial_backend.repository.EditorialRepository;
import com.example.booksocial_backend.service.impl.EditorialServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class EditorialServiceImplTest {

  @Mock
  private EditorialRepository editorialRepository;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private EditorialServiceImpl editorialService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // =========================
  // CREATE
  // =========================

  @Test
  void shouldCreateEditorialSuccessfully() {

    EditorialRequestDTO request = new EditorialRequestDTO("Shonen Jump", "JP");

    Editorial editorial = new Editorial();
    editorial.setName("Shonen Jump");
    editorial.setCountry("JP");

    Editorial saved = new Editorial();
    saved.setId(1L);
    saved.setName("Shonen Jump");
    saved.setCountry("JP");

    when(modelMapper.map(request, Editorial.class)).thenReturn(editorial);
    when(editorialRepository.existsByName("Shonen Jump")).thenReturn(false);
    when(editorialRepository.save(editorial)).thenReturn(saved);

    var result = editorialService.createEditorial(request);

    assertNotNull(result);
    assertEquals("Shonen Jump", result.getName());
  }

  @Test
  void shouldThrowExceptionWhenEditorialAlreadyExists() {

    EditorialRequestDTO request = new EditorialRequestDTO("Jump", "JP");

    Editorial editorial = new Editorial();
    editorial.setName("Jump");

    when(modelMapper.map(request, Editorial.class)).thenReturn(editorial);
    when(editorialRepository.existsByName("Jump")).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> {
      editorialService.createEditorial(request);
    });
  }

  @Test
  void shouldThrowExceptionWhenNameIsEmpty() {

    EditorialRequestDTO request = new EditorialRequestDTO("   ", "JP");

    Editorial editorial = new Editorial();
    editorial.setName("   ");

    when(modelMapper.map(request, Editorial.class)).thenReturn(editorial);

    assertThrows(IllegalArgumentException.class, () -> {
      editorialService.createEditorial(request);
    });
  }

  // =========================
  // GET
  // =========================

  @Test
  void shouldGetEditorialById() {

    Editorial editorial = new Editorial();
    editorial.setId(1L);
    editorial.setName("Jump");

    when(editorialRepository.findById(1L)).thenReturn(Optional.of(editorial));

    var result = editorialService.getEditorialById(1L);

    assertEquals("Jump", result.getName());
  }

  @Test
  void shouldThrowExceptionWhenEditorialNotFound() {

    when(editorialRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      editorialService.getEditorialById(1L);
    });
  }

  @Test
  void shouldGetAllEditorials() {

    Editorial editorial = new Editorial();
    editorial.setId(1L);

    when(editorialRepository.findAll()).thenReturn(List.of(editorial));

    var result = editorialService.getAllEditorials();

    assertEquals(1, result.size());
  }

  // =========================
  // UPDATE
  // =========================

  @Test
  void shouldUpdateEditorialSuccessfully() {

    Editorial existing = new Editorial();
    existing.setId(1L);
    existing.setName("Old");

    Editorial updated = new Editorial();
    updated.setName("New");
    updated.setCountry("JP");

    EditorialRequestDTO request = new EditorialRequestDTO("New", "JP");

    when(editorialRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(modelMapper.map(request, Editorial.class)).thenReturn(updated);
    when(editorialRepository.existsByName("New")).thenReturn(false);
    when(editorialRepository.save(existing)).thenReturn(existing);

    var result = editorialService.updateEditorial(1L, request);

    assertEquals("New", result.getName());
  }

  @Test
  void shouldThrowExceptionWhenDuplicateNameOnUpdate() {

    Editorial existing = new Editorial();
    existing.setId(1L);
    existing.setName("Old");

    Editorial updated = new Editorial();
    updated.setName("Duplicate");

    EditorialRequestDTO request = new EditorialRequestDTO("Duplicate", "JP");

    when(editorialRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(modelMapper.map(request, Editorial.class)).thenReturn(updated);
    when(editorialRepository.existsByName("Duplicate")).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> {
      editorialService.updateEditorial(1L, request);
    });
  }

  // =========================
  // DELETE
  // =========================

  @Test
  void shouldDeleteEditorial() {

    Editorial editorial = new Editorial();
    editorial.setId(1L);

    when(editorialRepository.findById(1L)).thenReturn(Optional.of(editorial));

    editorialService.deleteEditorial(1L);

    verify(editorialRepository).delete(editorial);
  }
}