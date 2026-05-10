package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.WorkFilterDTO;
import com.example.booksocial_backend.DTO.catalog.WorkRequestDTO;
import com.example.booksocial_backend.DTO.catalog.WorkResponseDTO;
import com.example.booksocial_backend.domain.catalog.Genre;
import com.example.booksocial_backend.domain.catalog.WorkType;
import com.example.booksocial_backend.service.WorkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de obras dentro del sistema BookSocial.
 *
 * @author Jorge
 * @version 1.1
 * @since 2026
 */
@RestController
@RequestMapping("/api/works")
@RequiredArgsConstructor
@Tag(name = "Work Controller", description = "API REST para la gestión del catálogo de obras")
public class WorkController {

  private final WorkService workService;

  // CREATE — solo ADMIN
  @Operation(summary = "Crear obra")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<WorkResponseDTO> createWork(@Valid @RequestBody WorkRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(workService.createWork(request));
  }

  @Operation(summary = "Creación masiva de obras")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/batch")
  public ResponseEntity<List<WorkResponseDTO>> createMany(@Valid @RequestBody List<WorkRequestDTO> requests) {
    return ResponseEntity.status(HttpStatus.CREATED).body(workService.createMany(requests));
  }

  // READ — público
  @Operation(summary = "Obtener obra por ID")
  @GetMapping("/{id}")
  public ResponseEntity<WorkResponseDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(workService.getWorkById(id));
  }

  @Operation(summary = "Listar todas las obras")
  @GetMapping
  public ResponseEntity<List<WorkResponseDTO>> getAll() {
    return ResponseEntity.ok(workService.getAllWorks());
  }

  @Operation(summary = "Búsqueda simple")
  @GetMapping("/search")
  public ResponseEntity<List<WorkResponseDTO>> search(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String genre,
      @RequestParam(required = false) Double rating) {
    return ResponseEntity.ok(workService.searchWorks(title, genre, rating));
  }

  @Operation(summary = "Obras por género")
  @GetMapping("/genre/{genre}")
  public ResponseEntity<List<WorkResponseDTO>> getByGenre(@PathVariable Genre genre) {
    return ResponseEntity.ok(workService.getWorksByGenre(genre));
  }

  @Operation(summary = "Obras por autor")
  @GetMapping("/author/{authorId}")
  public ResponseEntity<List<WorkResponseDTO>> getByAuthor(@PathVariable Long authorId) {
    return ResponseEntity.ok(workService.getWorksByAuthor(authorId));
  }

  @Operation(summary = "Top obras")
  @GetMapping("/top")
  public ResponseEntity<List<WorkResponseDTO>> getTopRated() {
    return ResponseEntity.ok(workService.getTopRatedWorks());
  }

  @Operation(summary = "Búsqueda avanzada")
  @GetMapping("/advanced-search")
  public ResponseEntity<Page<WorkResponseDTO>> advancedSearch(
      @RequestParam(required = false) Genre genre,
      @RequestParam(required = false) WorkType type,
      Pageable pageable) {
    WorkFilterDTO filter = new WorkFilterDTO();
    filter.setGenre(genre);
    filter.setType(type);
    return ResponseEntity.ok(workService.searchAdvanced(filter, pageable));
  }

  // UPDATE / DELETE — solo ADMIN
  @Operation(summary = "Actualizar obra")
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<WorkResponseDTO> update(@PathVariable Long id,
      @Valid @RequestBody WorkRequestDTO request) {
    return ResponseEntity.ok(workService.updateWork(id, request));
  }

  @Operation(summary = "Eliminar obra")
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    workService.deleteWork(id);
    return ResponseEntity.noContent().build();
  }
}
