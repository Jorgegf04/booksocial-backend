package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * <p>
 * La entidad Work representa el núcleo del catálogo, incluyendo libros, mangas
 * y cómics. Contiene la información principal de cada obra y se relaciona con
 * autores, ediciones y sistemas sociales.
 * </p>
 *
 * <p>
 * Este controlador permite realizar operaciones CRUD, búsquedas simples
 * y búsquedas avanzadas con filtros dinámicos y paginación.
 * </p>
 *
 * <p>
 * <b>Responsabilidades:</b>
 * </p>
 * <ul>
 * <li>Gestión completa de obras (CRUD)</li>
 * <li>Búsquedas simples (por título, género, rating)</li>
 * <li>Búsquedas por autor y género</li>
 * <li>Ranking de obras</li>
 * <li>Búsqueda avanzada con filtros y paginación</li>
 * </ul>
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

  // =========================
  // CREATE
  // =========================

  @Operation(summary = "Crear obra", description = "Registra una nueva obra en el sistema (libro, manga o cómic).")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Obra creada correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos")
  })
  @PostMapping
  public ResponseEntity<WorkResponseDTO> createWork(
      @Valid @RequestBody WorkRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(workService.createWork(request));
  }

  @Operation(summary = "Creación masiva de obras", description = "Permite registrar múltiples obras en una sola petición.")
  @PostMapping("/batch")
  public ResponseEntity<List<WorkResponseDTO>> createMany(
      @Valid @RequestBody List<WorkRequestDTO> requests) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(workService.createMany(requests));
  }

  // =========================
  // READ
  // =========================

  @Operation(summary = "Obtener obra por ID", description = "Recupera una obra específica mediante su identificador.")
  @GetMapping("/{id}")
  public ResponseEntity<WorkResponseDTO> getById(
      @Parameter(description = "ID de la obra", example = "1") @PathVariable Long id) {

    return ResponseEntity.ok(workService.getWorkById(id));
  }

  @Operation(summary = "Listar todas las obras", description = "Devuelve todas las obras registradas en el sistema.")
  @GetMapping
  public ResponseEntity<List<WorkResponseDTO>> getAll() {

    return ResponseEntity.ok(workService.getAllWorks());
  }

  /**
   * Búsqueda simple de obras.
   */
  @Operation(summary = "Búsqueda simple", description = "Permite buscar obras por título, género o valoración.")
  @GetMapping("/search")
  public ResponseEntity<List<WorkResponseDTO>> search(
      @Parameter(description = "Título parcial", example = "Naruto") @RequestParam(required = false) String title,

      @Parameter(description = "Género", example = "ACTION") @RequestParam(required = false) String genre,

      @Parameter(description = "Valoración mínima", example = "4.5") @RequestParam(required = false) Double rating) {

    return ResponseEntity.ok(workService.searchWorks(title, genre, rating));
  }

  @Operation(summary = "Obras por género", description = "Devuelve todas las obras de un género específico.")
  @GetMapping("/genre/{genre}")
  public ResponseEntity<List<WorkResponseDTO>> getByGenre(
      @Parameter(description = "Género", example = "ACTION") @PathVariable Genre genre) {

    return ResponseEntity.ok(workService.getWorksByGenre(genre));
  }

  @Operation(summary = "Obras por autor", description = "Devuelve todas las obras de un autor.")
  @GetMapping("/author/{authorId}")
  public ResponseEntity<List<WorkResponseDTO>> getByAuthor(
      @Parameter(description = "ID del autor", example = "1") @PathVariable Long authorId) {

    return ResponseEntity.ok(workService.getWorksByAuthor(authorId));
  }

  @Operation(summary = "Top obras", description = "Obtiene las obras mejor valoradas del sistema.")
  @GetMapping("/top")
  public ResponseEntity<List<WorkResponseDTO>> getTopRated() {

    return ResponseEntity.ok(workService.getTopRatedWorks());
  }

  // =========================
  // UPDATE
  // =========================

  @Operation(summary = "Actualizar obra", description = "Modifica los datos de una obra existente.")
  @PutMapping("/{id}")
  public ResponseEntity<WorkResponseDTO> update(
      @Parameter(description = "ID de la obra", example = "1") @PathVariable Long id,
      @Valid @RequestBody WorkRequestDTO request) {

    return ResponseEntity.ok(workService.updateWork(id, request));
  }

  // =========================
  // DELETE
  // =========================

  @Operation(summary = "Eliminar obra", description = "Elimina una obra del sistema.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "ID de la obra", example = "1") @PathVariable Long id) {

    workService.deleteWork(id);
    return ResponseEntity.noContent().build();
  }

  // =========================
  // ADVANCED SEARCH
  // =========================

  /**
   * Búsqueda avanzada con filtros dinámicos y paginación.
   *
   * <p>
   * Permite combinar múltiples criterios de búsqueda y obtener resultados
   * paginados para mejorar el rendimiento y la experiencia del usuario.
   * </p>
   */
  @Operation(summary = "Búsqueda avanzada", description = "Permite filtrar obras por múltiples criterios con soporte de paginación.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Resultados obtenidos correctamente")
  })
  @GetMapping("/advanced-search")
  public ResponseEntity<Page<WorkResponseDTO>> advancedSearch(
      @Parameter(description = "Género", example = "ACTION") @RequestParam(required = false) Genre genre,

      @Parameter(description = "Tipo de obra", example = "MANGA") @RequestParam(required = false) WorkType type,

      Pageable pageable) {

    WorkFilterDTO filter = new WorkFilterDTO();
    filter.setGenre(genre);
    filter.setType(type);

    return ResponseEntity.ok(workService.searchAdvanced(filter, pageable));
  }
}