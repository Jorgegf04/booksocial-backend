package com.example.booksocial_backend.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.catalog.ChapterResponseDTO;
import com.example.booksocial_backend.DTO.catalog.ChapterRequestDTO;
import com.example.booksocial_backend.domain.catalog.Chapter;
import com.example.booksocial_backend.domain.catalog.Tome;
import com.example.booksocial_backend.exception.ChapterAlreadyExistsException;
import com.example.booksocial_backend.exception.ChapterNotFoundException;
import com.example.booksocial_backend.exception.TomeNotFoundException;
import com.example.booksocial_backend.repository.ChapterRepository;
import com.example.booksocial_backend.repository.TomeRepository;
import com.example.booksocial_backend.service.ChapterService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ChapterServiceImpl implements ChapterService {

  private final ChapterRepository chapterRepository;
  private final TomeRepository tomeRepository;
  private final ModelMapper modelMapper;

  @Override
  public ChapterResponseDTO createChapter(ChapterRequestDTO request) {

    Chapter chapter = modelMapper.map(request, Chapter.class);

    Tome tome = tomeRepository.findById(request.getTomeId())
        .orElseThrow(() -> new TomeNotFoundException(request.getTomeId()));

    chapter.setTome(tome);

    validateChapter(chapter);

    List<Chapter> existingChapters = chapterRepository.findByTomeId(request.getTomeId());

    boolean exists = existingChapters.stream()
        .anyMatch(c -> c.getChapterNumber().equals(request.getChapterNumber()));

    if (exists) {
      throw new ChapterAlreadyExistsException(
          request.getChapterNumber(),
          request.getTomeId());
    }

    Chapter saved = chapterRepository.save(chapter);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public ChapterResponseDTO getChapterById(Long id) {

    Chapter chapter = getChapterEntityById(id);

    return mapToDTO(chapter);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChapterResponseDTO> getAllChapters() {

    return chapterRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChapterResponseDTO> getChaptersByTome(Long tomeId) {

    return chapterRepository.findByTomeId(tomeId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public void deleteChapter(Long id) {

    Chapter chapter = getChapterEntityById(id);

    chapterRepository.delete(chapter);
  }

  private ChapterResponseDTO mapToDTO(Chapter chapter) {
    Tome tome = chapter.getTome();
    return new ChapterResponseDTO(
        chapter.getId(),
        chapter.getChapterNumber(),
        chapter.getTitle(),
        tome != null ? tome.getId() : null,
        tome != null ? tome.getTitle() : null,
        (tome != null && tome.getEdition() != null) ? tome.getEdition().getTitle() : null);
  }

  private Chapter getChapterEntityById(Long id) {

    return chapterRepository.findById(id)
        .orElseThrow(() -> new ChapterNotFoundException(id));
  }

  private void validateChapter(Chapter chapter) {

    if (chapter == null) {
      throw new IllegalArgumentException("El capítulo no puede ser nulo");
    }

    if (chapter.getChapterNumber() == null || chapter.getChapterNumber() <= 0) {
      throw new IllegalArgumentException("El número de capítulo debe ser positivo");
    }

    if (chapter.getTitle() == null || chapter.getTitle().trim().isEmpty()) {
      throw new IllegalArgumentException("El título del capítulo es obligatorio");
    }

    if (chapter.getTome() == null || chapter.getTome().getId() == null) {
      throw new IllegalArgumentException("El capítulo debe estar asociado a un tomo válido");
    }
  }
}