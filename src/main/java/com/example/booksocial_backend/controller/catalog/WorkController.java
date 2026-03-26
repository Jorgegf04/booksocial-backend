package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.booksocial_backend.DTO.catalog.WorkRequestDTO;
import com.example.booksocial_backend.DTO.catalog.WorkResponseDTO;
import com.example.booksocial_backend.service.WorkService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/works")
@RequiredArgsConstructor
@Tag(name = "Work Controller", description = "Gestión del catálogo de obras")
public class WorkController {

  private final WorkService workService;

  // =========================
  // CREATE
  // =========================

  @PostMapping
  public ResponseEntity<WorkResponseDTO> createWork(@RequestBody WorkRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(workService.createWork(request));
  }

  @PostMapping("/batch")
  public ResponseEntity<List<WorkResponseDTO>> createMany(
      @RequestBody List<WorkRequestDTO> requests) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(workService.createMany(requests));
  }

  // =========================
  // READ
  // =========================

  @GetMapping("/{id}")
  public ResponseEntity<WorkResponseDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(workService.getWorkById(id));
  }

  @GetMapping
  public ResponseEntity<List<WorkResponseDTO>> getAll() {
    return ResponseEntity.ok(workService.getAllWorks());
  }

  @GetMapping("/search")
  public ResponseEntity<List<WorkResponseDTO>> search(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String genre,
      @RequestParam(required = false) Double rating) {

    return ResponseEntity.ok(workService.searchWorks(title, genre, rating));
  }

  @GetMapping("/genre/{genre}")
  public ResponseEntity<List<WorkResponseDTO>> getByGenre(@PathVariable String genre) {
    return ResponseEntity.ok(workService.getWorksByGenre(genre));
  }

  @GetMapping("/author/{authorId}")
  public ResponseEntity<List<WorkResponseDTO>> getByAuthor(@PathVariable Long authorId) {
    return ResponseEntity.ok(workService.getWorksByAuthor(authorId));
  }

  @GetMapping("/top")
  public ResponseEntity<List<WorkResponseDTO>> getTopRated() {
    return ResponseEntity.ok(workService.getTopRatedWorks());
  }

  // =========================
  // UPDATE
  // =========================

  @PutMapping("/{id}")
  public ResponseEntity<WorkResponseDTO> update(
      @PathVariable Long id,
      @RequestBody WorkRequestDTO request) {

    return ResponseEntity.ok(workService.updateWork(id, request));
  }

  // =========================
  // DELETE
  // =========================

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    workService.deleteWork(id);
    return ResponseEntity.noContent().build();
  }
}