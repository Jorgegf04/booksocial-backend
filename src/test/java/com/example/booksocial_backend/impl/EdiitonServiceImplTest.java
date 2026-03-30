package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.booksocial_backend.DTO.catalog.EditionRequestDTO;
import com.example.booksocial_backend.domain.catalog.Edition;
import com.example.booksocial_backend.domain.catalog.Editorial;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.repository.EditionRepository;
import com.example.booksocial_backend.repository.EditorialRepository;
import com.example.booksocial_backend.repository.WorkRepository;
import com.example.booksocial_backend.service.impl.EditionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SpringBootTest
class EditionServiceImplTest {

  @Mock
  private EditionRepository editionRepository;

  @Mock
  private WorkRepository workRepository;

  @Mock
  private EditorialRepository editorialRepository;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private EditionServiceImpl editionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // =========================
  // CREATE
  // =========================

  @Test
  void shouldCreateEditionSuccessfully() {

    EditionRequestDTO request = new EditionRequestDTO(
        "ISBN123",
        LocalDate.now(),
        1L,
        1L,
        "Naruto Vol 1",
        10);

    Work work = new Work();
    work.setId(1L);
    work.setTitle("Naruto");

    Editorial editorial = new Editorial();
    editorial.setId(1L);
    editorial.setName("Shonen Jump");

    Edition saved = new Edition();
    saved.setId(1L);
    saved.setIsbn("ISBN123");
    saved.setWork(work);
    saved.setEditorial(editorial);
    saved.setTitle("Naruto Vol 1");

    when(workRepository.findById(1L)).thenReturn(Optional.of(work));
    when(editorialRepository.findById(1L)).thenReturn(Optional.of(editorial));
    when(editionRepository.save(any())).thenReturn(saved);

    var result = editionService.createEdition(request);

    assertNotNull(result);
    assertEquals("ISBN123", result.getIsbn());
  }

  @Test
  void shouldThrowExceptionWhenWorkNotFound() {

    EditionRequestDTO request = new EditionRequestDTO(
        "ISBN123", LocalDate.now(), 1L, 1L, "Titulo", 5);

    when(workRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      editionService.createEdition(request);
    });
  }

  @Test
  void shouldThrowExceptionWhenEditorialNotFound() {

    EditionRequestDTO request = new EditionRequestDTO(
        "ISBN123", LocalDate.now(), 1L, 1L, "Titulo", 5);

    when(workRepository.findById(1L)).thenReturn(Optional.of(new Work()));
    when(editorialRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      editionService.createEdition(request);
    });
  }

  // =========================
  // GET
  // =========================

  @Test
  void shouldGetEditionById() {

    Work work = new Work();
    work.setId(1L);
    work.setTitle("Naruto");

    Editorial editorial = new Editorial();
    editorial.setId(1L);
    editorial.setName("Jump");

    Edition edition = new Edition();
    edition.setId(1L);
    edition.setIsbn("ISBN123");
    edition.setWork(work);
    edition.setEditorial(editorial);

    when(editionRepository.findById(1L)).thenReturn(Optional.of(edition));

    var result = editionService.getEditionById(1L);

    assertEquals("ISBN123", result.getIsbn());
  }

  @Test
  void shouldThrowExceptionWhenEditionNotFound() {

    when(editionRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      editionService.getEditionById(1L);
    });
  }

  @Test
  void shouldGetAllEditions() {

    Edition edition = new Edition();
    edition.setId(1L);

    when(editionRepository.findAll()).thenReturn(List.of(edition));

    var result = editionService.getAllEditions();

    assertEquals(1, result.size());
  }

  // =========================
  // UPDATE
  // =========================
  @Test
  void shouldUpdateEditionSuccessfully() {

    Edition existing = new Edition();
    existing.setId(1L);
    existing.setIsbn("OLDISBN");

    Work work = new Work();
    work.setId(1L);

    Editorial editorial = new Editorial();
    editorial.setId(1L);

    Edition updated = new Edition();
    updated.setIsbn("NEWISBN");
    updated.setWork(work);
    updated.setEditorial(editorial);

    EditionRequestDTO request = new EditionRequestDTO(
        "NEWISBN", LocalDate.now(), 1L, 1L, "Titulo", 5);

    when(editionRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(modelMapper.map(request, Edition.class)).thenReturn(updated);
    when(workRepository.findById(1L)).thenReturn(Optional.of(work));
    when(editorialRepository.findById(1L)).thenReturn(Optional.of(editorial));
    when(editionRepository.findByIsbn("NEWISBN")).thenReturn(Optional.empty());
    when(editionRepository.save(existing)).thenReturn(existing);

    var result = editionService.updateEdition(1L, request);

    assertEquals("NEWISBN", result.getIsbn());
  }

  @Test
  void shouldThrowExceptionWhenDuplicateISBN() {

    Edition existing = new Edition();
    existing.setId(1L);
    existing.setIsbn("OLDISBN");

    Edition updated = new Edition();
    updated.setIsbn("DUPLICATE");

    EditionRequestDTO request = new EditionRequestDTO(
        "DUPLICATE", LocalDate.now(), 1L, 1L, "Titulo", 5);

    when(editionRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(modelMapper.map(request, Edition.class)).thenReturn(updated);
    when(workRepository.findById(1L)).thenReturn(Optional.of(new Work()));
    when(editorialRepository.findById(1L)).thenReturn(Optional.of(new Editorial()));
    when(editionRepository.findByIsbn("DUPLICATE")).thenReturn(Optional.of(new Edition()));

    assertThrows(IllegalArgumentException.class, () -> {
      editionService.updateEdition(1L, request);
    });
  }

  // =========================
  // DELETE
  // =========================

  @Test
  void shouldDeleteEdition() {

    Edition edition = new Edition();
    edition.setId(1L);

    when(editionRepository.findById(1L)).thenReturn(Optional.of(edition));

    editionService.deleteEdition(1L);

    verify(editionRepository).delete(edition);
  }
}