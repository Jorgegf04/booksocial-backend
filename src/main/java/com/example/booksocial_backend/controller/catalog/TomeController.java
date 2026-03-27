package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.TomeRequestDTO;
import com.example.booksocial_backend.DTO.catalog.TomeResponseDTO;
import com.example.booksocial_backend.service.TomeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tomes")
@RequiredArgsConstructor
@Tag(name = "Tome Controller", description = "Gestión de tomos (estructura de mangas)")
public class TomeController {

  private final TomeService tomeService;

  // =========================
  // CREATE
  // =========================

  @PostMapping
  public ResponseEntity<TomeResponseDTO> create(@RequestBody TomeRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(tomeService.createTome(request));
  }

  // =========================
  // READ
  // =========================

  @GetMapping("/{id}")
  public ResponseEntity<TomeResponseDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(tomeService.getTomeById(id));
  }

  @GetMapping
  public ResponseEntity<List<TomeResponseDTO>> getAll() {
    return ResponseEntity.ok(tomeService.getAllTomes());
  }

  @GetMapping("/edition/{editionId}")
  public ResponseEntity<List<TomeResponseDTO>> getByEdition(@PathVariable Long editionId) {
    return ResponseEntity.ok(tomeService.getTomesByEdition(editionId));
  }

  @GetMapping("/edition/{editionId}/ordered")
  public ResponseEntity<List<TomeResponseDTO>> getOrdered(@PathVariable Long editionId) {
    return ResponseEntity.ok(tomeService.getTomesByEditionOrdered(editionId));
  }

  // =========================
  // UPDATE
  // =========================

  @PutMapping("/{id}")
  public ResponseEntity<TomeResponseDTO> update(
      @PathVariable Long id,
      @RequestBody TomeRequestDTO request) {

    return ResponseEntity.ok(tomeService.updateTome(id, request));
  }

  // =========================
  // DELETE
  // =========================

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    tomeService.deleteTome(id);
    return ResponseEntity.noContent().build();
  }
}