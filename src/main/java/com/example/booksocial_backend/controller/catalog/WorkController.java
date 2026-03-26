package com.example.booksocial_backend.controller.catalog;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.WorkRequestDTO;
import com.example.booksocial_backend.DTO.catalog.WorkResponseDTO;
import com.example.booksocial_backend.service.WorkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión del catálogo de obras.
 *
 * Permite realizar operaciones CRUD y consultas avanzadas
 * sobre las obras disponibles en el sistema.
 */
@Tag(name = "Work Controller", description = "Gestión completa del catálogo de obras literarias del sistema BookSocial")
@RestController
@RequestMapping("/api/works")
@RequiredArgsConstructor
public class WorkController {

  private final WorkService workService;

  // =========================
  // CREATE
  // =========================

  @Operation(summary = "Crear nueva obra", description = "Registra una nueva obra en el sistema. "
      + "Permite asociar autores mediante sus identificadores.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Obra creada correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "500", description = "Error interno")
  })
  @PostMapping
  public ResponseEntity<WorkResponseDTO> createWork(@RequestBody WorkRequestDTO request) {
    WorkResponseDTO work = workService.createWork(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(work);
  }

  @Operation(summary = "Crear múltiples obras", description = "Permite registrar varias obras en una única petición. "
      + "Ideal para carga inicial de catálogo.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Obras creadas correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista vacía o inválida")
  })
  @PostMapping("/batch")
  public ResponseEntity<List<WorkResponseDTO>> createMany(
      @RequestBody List<WorkRequestDTO> requests) {

    List<WorkResponseDTO> works = requests.stream()
        .map(workService::createWork)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(works);
  }

  // =========================
  // READ
  // =========================

  @Operation(summary = "Obtener obra por ID", description = "Recupera una obra específica mediante su identificador.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Obra encontrada"),
      @ApiResponse(responseCode = "404", description = "Obra no encontrada")
  })
  @GetMapping("/{id}")
  public ResponseEntity<WorkResponseDTO> getWorkById(@PathVariable Long id) {
    return ResponseEntity.ok(workService.getWorkById(id));
  }

  @Operation(summary = "Obtener todas las obras", description = "Devuelve el catálogo completo de obras registradas.")
  @GetMapping
  public ResponseEntity<List<WorkResponseDTO>> getAllWorks() {
    return ResponseEntity.ok(workService.getAllWorks());
  }

  @Operation(summary = "Buscar obras por título", description = "Realiza una búsqueda parcial de obras por título.")
  @GetMapping("/search/title")
  public ResponseEntity<List<WorkResponseDTO>> searchByTitle(@RequestParam String title) {
    return ResponseEntity.ok(workService.searchWorksByTitle(title));
  }

  @Operation(summary = "Filtrar por género", description = "Devuelve obras filtradas por género literario.")
  @GetMapping("/genre/{genre}")
  public ResponseEntity<List<WorkResponseDTO>> getByGenre(@PathVariable String genre) {
    return ResponseEntity.ok(workService.getWorksByGenre(genre));
  }

  @Operation(summary = "Buscar obras por autor", description = "Obtiene todas las obras asociadas a un autor concreto.")
  @GetMapping("/author/{authorId}")
  public ResponseEntity<List<WorkResponseDTO>> getByAuthor(@PathVariable Long authorId) {
    return ResponseEntity.ok(workService.getWorksByAuthor(authorId));
  }

  @Operation(summary = "Búsqueda avanzada de obras", description = "Permite filtrar obras por múltiples criterios: título, género y valoración.")
  @GetMapping("/search")
  public ResponseEntity<List<WorkResponseDTO>> searchWorks(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String genre,
      @RequestParam(required = false) Double rating) {

    return ResponseEntity.ok(workService.searchWorks(title, genre, rating));
  }

  @Operation(summary = "Top obras mejor valoradas", description = "Devuelve las obras con mayor valoración media.")
  @GetMapping("/top")
  public ResponseEntity<List<WorkResponseDTO>> getTopRated() {
    return ResponseEntity.ok(workService.getTopRatedWorks());
  }

  @Operation(summary = "Obras posteriores a una fecha", description = "Filtra obras cuya fecha de publicación es posterior a la indicada.")
  @GetMapping("/after")
  public ResponseEntity<List<WorkResponseDTO>> getAfterDate(@RequestParam String date) {
    return ResponseEntity.ok(workService.getWorksAfterDate(LocalDate.parse(date)));
  }

  // =========================
  // UPDATE
  // =========================

  @Operation(summary = "Actualizar obra", description = "Permite modificar los datos de una obra existente.")
  @PutMapping("/{id}")
  public ResponseEntity<WorkResponseDTO> updateWork(
      @PathVariable Long id,
      @RequestBody WorkRequestDTO request) {

    return ResponseEntity.ok(workService.updateWork(id, request));
  }

  // =========================
  // DELETE
  // =========================

  @Operation(summary = "Eliminar obra", description = "Elimina una obra del catálogo. Esta operación es irreversible.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteWork(@PathVariable Long id) {
    workService.deleteWork(id);
    return ResponseEntity.noContent().build();
  }
}