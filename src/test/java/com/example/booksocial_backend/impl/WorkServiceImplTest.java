package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import com.example.booksocial_backend.DTO.catalog.WorkRequestDTO;
import com.example.booksocial_backend.domain.catalog.Author;
import com.example.booksocial_backend.domain.catalog.Genre;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.repository.AuthorRepository;
import com.example.booksocial_backend.repository.WorkRepository;
import com.example.booksocial_backend.service.impl.WorkServiceImpl;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WorkServiceImplTest {

  @Mock
  private WorkRepository workRepository;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private AuthorRepository authorRepository;

  @InjectMocks
  private WorkServiceImpl service;

  private Work work;
  private Author author;

  @BeforeEach
  void setUp() {

    author = new Author();
    author.setId(1L);
    author.setName("Masashi Kishimoto");

    work = new Work();
    work.setId(1L);
    work.setTitle("Naruto");
    work.setGenre(Genre.ACTION);
    work.setAuthors(new ArrayList<>(List.of(author)));
  }

  // =========================
  // CREATE
  // =========================

  @Test
  void shouldCreateWorkSuccessfully() {

    WorkRequestDTO request = new WorkRequestDTO(
        "Naruto", "Descripción", Genre.ACTION, null, null, null, null, null, List.of("Masashi Kishimoto"));

    when(modelMapper.map(request, Work.class)).thenReturn(work);
    when(authorRepository.findByNameIgnoreCase("Masashi Kishimoto")).thenReturn(Optional.of(author));
    when(workRepository.save(any())).thenReturn(work);

    var result = service.createWork(request);

    assertNotNull(result);
    assertEquals("Naruto", result.getTitle());
  }

  @Test
  void shouldThrowExceptionWhenTitleIsNull() {

    WorkRequestDTO request = new WorkRequestDTO(
        null, "Descripción", Genre.ACTION, null, null, null, null, null, null);

    assertThrows(IllegalArgumentException.class, () -> service.createWork(request));
  }

  @Test
  void shouldThrowExceptionWhenGenreIsNull() {

    WorkRequestDTO request = new WorkRequestDTO(
        "Naruto", "Descripción", null, null, null, null, null, null, null);

    assertThrows(IllegalArgumentException.class, () -> service.createWork(request));
  }

  @Test
  void shouldThrowExceptionWhenAuthorNotFound() {

    WorkRequestDTO request = new WorkRequestDTO(
        "Naruto", null, Genre.ACTION, null, null, null, null, null, List.of("Autor Desconocido"));

    when(modelMapper.map(request, Work.class)).thenReturn(work);
    when(authorRepository.findByNameIgnoreCase("Autor Desconocido")).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> service.createWork(request));
  }

  // =========================
  // READ
  // =========================

  @Test
  void shouldGetWorkById() {

    when(workRepository.findById(1L)).thenReturn(Optional.of(work));

    var result = service.getWorkById(1L);

    assertEquals(1L, result.getId());
    assertEquals("Naruto", result.getTitle());
  }

  @Test
  void shouldThrowExceptionWhenWorkNotFound() {

    when(workRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> service.getWorkById(1L));
  }

  @Test
  void shouldGetAllWorks() {

    when(workRepository.findAll()).thenReturn(List.of(work));

    var result = service.getAllWorks();

    assertEquals(1, result.size());
    assertEquals("Naruto", result.get(0).getTitle());
  }

  @Test
  void shouldSearchWorksByTitle() {

    when(workRepository.findByTitleContainingIgnoreCase("Naruto")).thenReturn(List.of(work));

    var result = service.searchWorksByTitle("Naruto");

    assertEquals(1, result.size());
  }

  @Test
  void shouldGetWorksByGenre() {

    when(workRepository.findByGenre(Genre.ACTION)).thenReturn(List.of(work));

    var result = service.getWorksByGenre(Genre.ACTION);

    assertEquals(1, result.size());
    assertEquals(Genre.ACTION, result.get(0).getGenre());
  }

  // =========================
  // UPDATE
  // =========================

  @Test
  void shouldUpdateWorkSuccessfully() {

    WorkRequestDTO request = new WorkRequestDTO(
        "Naruto Shippuden", null, null, null, null, null, null, null, null);

    when(workRepository.findById(1L)).thenReturn(Optional.of(work));
    when(workRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = service.updateWork(1L, request);

    assertEquals("Naruto Shippuden", result.getTitle());
  }

  @Test
  void shouldThrowExceptionWhenUpdatingNonExistingWork() {

    WorkRequestDTO request = new WorkRequestDTO(
        "Naruto Shippuden", null, null, null, null, null, null, null, null);

    when(workRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> service.updateWork(1L, request));
  }

  // =========================
  // DELETE
  // =========================

  @Test
  void shouldDeleteWork() {

    when(workRepository.findById(1L)).thenReturn(Optional.of(work));

    service.deleteWork(1L);

    verify(workRepository).delete(work);
  }

  @Test
  void shouldThrowExceptionWhenDeletingNonExistingWork() {

    when(workRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> service.deleteWork(1L));
  }

  // =========================
  // BATCH CREATE
  // =========================

  @Test
  void shouldThrowExceptionWhenCreateManyEmpty() {

    assertThrows(IllegalArgumentException.class, () -> service.createMany(List.of()));
  }
}
