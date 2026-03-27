package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.ChapterRequestDTO;
import com.example.booksocial_backend.DTO.catalog.ChapterResponseDTO;
import com.example.booksocial_backend.service.ChapterService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
@Tag(name = "Chapter Controller", description = "Gestión de capítulos dentro de tomos")
public class ChapterController {

  private final ChapterService chapterService;

  // =========================
  // CREATE
  // =========================

  @PostMapping
  public ResponseEntity<ChapterResponseDTO> createChapter(
      @RequestBody ChapterRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(chapterService.createChapter(request));
  }

  @PostMapping("/batch")
  public ResponseEntity<List<ChapterResponseDTO>> createMany(
      @RequestBody List<ChapterRequestDTO> requests) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(requests.stream()
            .map(chapterService::createChapter)
            .toList());
  }

  // =========================
  // READ
  // =========================

  @GetMapping("/{id}")
  public ResponseEntity<ChapterResponseDTO> getById(@PathVariable Long id) {

    return ResponseEntity.ok(chapterService.getChapterById(id));
  }

  @GetMapping
  public ResponseEntity<List<ChapterResponseDTO>> getAll() {

    return ResponseEntity.ok(chapterService.getAllChapters());
  }

  @GetMapping("/tome/{tomeId}")
  public ResponseEntity<List<ChapterResponseDTO>> getByTome(
      @PathVariable Long tomeId) {

    return ResponseEntity.ok(chapterService.getChaptersByTome(tomeId));
  }

  // =========================
  // DELETE
  // =========================

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {

    chapterService.deleteChapter(id);
    return ResponseEntity.noContent().build();
  }
}