package com.example.booksocial_backend.controller.catalog;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.CreateWorkRequest;
import com.example.booksocial_backend.DTO.catalog.WorkDTO;
import com.example.booksocial_backend.service.WorkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión del catálogo de obras.
 *
 * Permite realizar operaciones CRUD y consultas avanzadas
 * sobre las obras disponibles en el sistema.
 */
@RestController
@RequestMapping("/api/works")
@RequiredArgsConstructor
@Tag(name = "Work Controller", description = "Gestión del catálogo de obras")
public class WorkController {

  private final WorkService workService;

  // =========================
  // CREATE
  // =========================

  @Operation(summary = "Crear nueva obra")
  @PostMapping
  public ResponseEntity<WorkDTO> createWork(@RequestBody CreateWorkRequest request) {
    WorkDTO work = workService.createWork(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(work);
  }

  @PostMapping("/batch")
  public ResponseEntity<List<WorkDTO>> createMany(
      @RequestBody List<CreateWorkRequest> requests) {

    List<WorkDTO> works = requests.stream()
        .map(workService::createWork)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(works);
  }

  // =========================
  // READ
  // =========================

  @Operation(summary = "Obtener obra por ID")
  @GetMapping("/{id}")
  public ResponseEntity<WorkDTO> getWorkById(@PathVariable Long id) {
    return ResponseEntity.ok(workService.getWorkById(id));
  }

  @Operation(summary = "Obtener todas las obras")
  @GetMapping
  public ResponseEntity<List<WorkDTO>> getAllWorks() {
    return ResponseEntity.ok(workService.getAllWorks());
  }

  @Operation(summary = "Buscar obras por título")
  @GetMapping("/search/title")
  public ResponseEntity<List<WorkDTO>> searchByTitle(@RequestParam String title) {
    return ResponseEntity.ok(workService.searchWorksByTitle(title));
  }

  @Operation(summary = "Filtrar por género")
  @GetMapping("/genre/{genre}")
  public ResponseEntity<List<WorkDTO>> getByGenre(@PathVariable String genre) {
    return ResponseEntity.ok(workService.getWorksByGenre(genre));
  }

  @Operation(summary = "Buscar por autor")
  @GetMapping("/author/{authorId}")
  public ResponseEntity<List<WorkDTO>> getByAuthor(@PathVariable Long authorId) {
    return ResponseEntity.ok(workService.getWorksByAuthor(authorId));
  }

  @Operation(summary = "Búsqueda avanzada")
  @GetMapping("/search")
  public ResponseEntity<List<WorkDTO>> searchWorks(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String genre,
      @RequestParam(required = false) Double rating) {

    return ResponseEntity.ok(workService.searchWorks(title, genre, rating));
  }

  @Operation(summary = "Top obras mejor valoradas")
  @GetMapping("/top")
  public ResponseEntity<List<WorkDTO>> getTopRated() {
    return ResponseEntity.ok(workService.getTopRatedWorks());
  }

  @Operation(summary = "Obras después de una fecha")
  @GetMapping("/after")
  public ResponseEntity<List<WorkDTO>> getAfterDate(@RequestParam String date) {
    return ResponseEntity.ok(workService.getWorksAfterDate(LocalDate.parse(date)));
  }

  // =========================
  // UPDATE
  // =========================

  @Operation(summary = "Actualizar obra")
  @PutMapping("/{id}")
  public ResponseEntity<WorkDTO> updateWork(
      @PathVariable Long id,
      @RequestBody CreateWorkRequest request) {

    return ResponseEntity.ok(workService.updateWork(id, request));
  }

  // =========================
  // DELETE
  // =========================

  @Operation(summary = "Eliminar obra")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteWork(@PathVariable Long id) {
    workService.deleteWork(id);
    return ResponseEntity.noContent().build();
  }

}