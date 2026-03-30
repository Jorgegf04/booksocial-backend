package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.example.booksocial_backend.DTO.catalog.ChapterRequestDTO;
import com.example.booksocial_backend.domain.catalog.Chapter;
import com.example.booksocial_backend.domain.catalog.Tome;
import com.example.booksocial_backend.domain.catalog.Edition;
import com.example.booksocial_backend.exception.ChapterAlreadyExistsException;
import com.example.booksocial_backend.exception.ChapterNotFoundException;
import com.example.booksocial_backend.repository.ChapterRepository;
import com.example.booksocial_backend.repository.TomeRepository;
import com.example.booksocial_backend.service.impl.ChapterServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ChapterServiceImplTest {

  @Mock
  private ChapterRepository chapterRepository;

  @Mock
  private TomeRepository tomeRepository;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private ChapterServiceImpl chapterService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // =========================
  // CREATE CHAPTER
  // =========================

  @Test
  void shouldCreateChapterSuccessfully() {

    ChapterRequestDTO request = new ChapterRequestDTO(1, "Capítulo 1", 1L);

    Tome tome = Tome.builder()
        .id(1L)
        .title("Tomo 1")
        .edition(Edition.builder().title("Edición 1").build())
        .build();

    Chapter chapter = Chapter.builder()
        .chapterNumber(1)
        .title("Capítulo 1")
        .build();

    when(modelMapper.map(request, Chapter.class)).thenReturn(chapter);
    when(tomeRepository.findById(1L)).thenReturn(Optional.of(tome));
    when(chapterRepository.findByTomeId(1L)).thenReturn(List.of());
    when(chapterRepository.save(any())).thenReturn(chapter);

    chapter.setTome(tome);

    var result = chapterService.createChapter(request);

    assertNotNull(result);
    assertEquals(1, result.getChapterNumber());
  }

  @Test
  void shouldThrowExceptionWhenChapterAlreadyExists() {

    ChapterRequestDTO request = new ChapterRequestDTO(1, "Capítulo 1", 1L);

    Tome tome = Tome.builder().id(1L).build();

    Chapter existing = Chapter.builder()
        .chapterNumber(1)
        .build();

    Chapter chapter = Chapter.builder()
        .chapterNumber(1)
        .title("Capítulo 1")
        .build();

    when(modelMapper.map(request, Chapter.class)).thenReturn(chapter);
    when(tomeRepository.findById(1L)).thenReturn(Optional.of(tome));
    when(chapterRepository.findByTomeId(1L)).thenReturn(List.of(existing));

    assertThrows(ChapterAlreadyExistsException.class, () -> {
      chapterService.createChapter(request);
    });
  }

  @Test
  void shouldThrowExceptionWhenTomeNotFound() {

    ChapterRequestDTO request = new ChapterRequestDTO(1, "Capítulo 1", 1L);

    when(modelMapper.map(request, Chapter.class)).thenReturn(new Chapter());
    when(tomeRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      chapterService.createChapter(request);
    });
  }

  @Test
  void shouldThrowExceptionWhenInvalidChapterNumber() {

    ChapterRequestDTO request = new ChapterRequestDTO(0, "Capítulo", 1L);

    Chapter chapter = Chapter.builder().chapterNumber(0).title("Capítulo").build();

    Tome tome = Tome.builder().id(1L).build();

    when(modelMapper.map(request, Chapter.class)).thenReturn(chapter);
    when(tomeRepository.findById(1L)).thenReturn(Optional.of(tome));

    assertThrows(IllegalArgumentException.class, () -> {
      chapterService.createChapter(request);
    });
  }

  // =========================
  // GET CHAPTER
  // =========================

  @Test
  void shouldGetChapterById() {

    Tome tome = Tome.builder()
        .id(1L)
        .title("Tomo 1")
        .edition(Edition.builder().title("Edición 1").build())
        .build();

    Chapter chapter = Chapter.builder()
        .id(1L)
        .chapterNumber(1)
        .title("Capítulo 1")
        .tome(tome)
        .build();

    when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

    var result = chapterService.getChapterById(1L);

    assertEquals("Capítulo 1", result.getTitle());
  }

  @Test
  void shouldThrowExceptionWhenChapterNotFound() {

    when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ChapterNotFoundException.class, () -> {
      chapterService.getChapterById(1L);
    });
  }

  // =========================
  // GET ALL
  // =========================

  @Test
  void shouldReturnAllChapters() {

    Tome tome = Tome.builder()
        .id(1L)
        .title("Tomo")
        .edition(Edition.builder().title("Edición").build())
        .build();

    Chapter chapter = Chapter.builder()
        .id(1L)
        .chapterNumber(1)
        .title("Capítulo")
        .tome(tome)
        .build();

    when(chapterRepository.findAll()).thenReturn(List.of(chapter));

    var result = chapterService.getAllChapters();

    assertEquals(1, result.size());
  }

  // =========================
  // DELETE
  // =========================

  @Test
  void shouldDeleteChapter() {

    Chapter chapter = Chapter.builder().id(1L).build();

    when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

    chapterService.deleteChapter(1L);

    verify(chapterRepository).delete(chapter);
  }
}