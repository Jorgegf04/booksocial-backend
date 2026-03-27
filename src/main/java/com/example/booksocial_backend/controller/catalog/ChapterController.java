package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.ChapterRequestDTO;
import com.example.booksocial_backend.DTO.catalog.ChapterResponseDTO;
import com.example.booksocial_backend.service.ChapterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST encargado de la gestión de capítulos dentro del sistema
 * BookSocial.
 *
 * <p>
 * Este controlador forma parte del módulo de catálogo, específicamente del
 * modelo
 * de obras complejas (mangas/cómics), donde los capítulos se organizan dentro
 * de tomos.
 * </p>
 *
 * <p>
 * Permite realizar operaciones de creación, consulta y eliminación de
 * capítulos,
 * facilitando la estructuración detallada de contenido editorial.
 * </p>
 *
 * <p>
 * <b>Responsabilidades:</b>
 * </p>
 * <ul>
 * <li>Crear capítulos individuales o en lote</li>
 * <li>Consultar capítulos por ID o por tomo</li>
 * <li>Listar todos los capítulos</li>
 * <li>Eliminar capítulos</li>
 * </ul>
 *
 * <p>
 * Todos los endpoints siguen principios REST y devuelven respuestas en formato
 * JSON.
 * </p>
 *
 * @author Jorge
 * @version 1.1
 * @since 2026
 */
@RestController
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
@Tag(name = "Chapter Controller", description = "API REST para la gestión de capítulos dentro de tomos en el catálogo")
public class ChapterController {

  private final ChapterService chapterService;

  // =========================
  // CREATE
  // =========================

  /**
   * Crea un nuevo capítulo dentro de un tomo.
   *
   * @param request DTO con la información del capítulo
   * @return capítulo creado
   */
  @Operation(summary = "Crear capítulo", description = "Registra un nuevo capítulo dentro de un tomo específico.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Capítulo creado correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping
  public ResponseEntity<ChapterResponseDTO> createChapter(
      @Valid @RequestBody ChapterRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(chapterService.createChapter(request));
  }

  /**
   * Crea múltiples capítulos en una única operación.
   *
   * @param requests lista de capítulos
   * @return lista de capítulos creados
   */
  @Operation(summary = "Creación masiva de capítulos", description = "Permite registrar múltiples capítulos en una sola petición. Útil para cargas iniciales de contenido.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Capítulos creados correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista inválida o vacía"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping("/batch")
  public ResponseEntity<List<ChapterResponseDTO>> createMany(
      @Valid @RequestBody List<ChapterRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista de capítulos no puede estar vacía");
    }

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(requests.stream()
            .map(chapterService::createChapter)
            .toList());
  }

  // =========================
  // READ
  // =========================

  /**
   * Obtiene un capítulo por su ID.
   *
   * @param id identificador del capítulo
   * @return capítulo encontrado
   */
  @Operation(summary = "Obtener capítulo por ID", description = "Recupera la información de un capítulo específico mediante su identificador.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Capítulo encontrado"),
      @ApiResponse(responseCode = "404", description = "Capítulo no encontrado")
  })
  @GetMapping("/{id}")
  public ResponseEntity<ChapterResponseDTO> getById(
      @Parameter(description = "ID único del capítulo", example = "1") @PathVariable Long id) {

    return ResponseEntity.ok(chapterService.getChapterById(id));
  }

  /**
   * Obtiene todos los capítulos del sistema.
   *
   * @return lista de capítulos
   */
  @Operation(summary = "Listar todos los capítulos", description = "Devuelve todos los capítulos registrados en el sistema.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
  })
  @GetMapping
  public ResponseEntity<List<ChapterResponseDTO>> getAll() {

    return ResponseEntity.ok(chapterService.getAllChapters());
  }

  /**
   * Obtiene los capítulos asociados a un tomo.
   *
   * @param tomeId ID del tomo
   * @return lista de capítulos del tomo
   */
  @Operation(summary = "Obtener capítulos por tomo", description = "Devuelve todos los capítulos asociados a un tomo específico.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Capítulos encontrados"),
      @ApiResponse(responseCode = "404", description = "Tomo no encontrado")
  })
  @GetMapping("/tome/{tomeId}")
  public ResponseEntity<List<ChapterResponseDTO>> getByTome(
      @Parameter(description = "ID del tomo", example = "10") @PathVariable Long tomeId) {

    return ResponseEntity.ok(chapterService.getChaptersByTome(tomeId));
  }

  // =========================
  // DELETE
  // =========================

  /**
   * Elimina un capítulo del sistema.
   *
   * @param id ID del capítulo
   * @return respuesta sin contenido
   */
  @Operation(summary = "Eliminar capítulo", description = "Elimina permanentemente un capítulo del sistema.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Capítulo eliminado correctamente"),
      @ApiResponse(responseCode = "404", description = "Capítulo no encontrado")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "ID del capítulo a eliminar", example = "1") @PathVariable Long id) {

    chapterService.deleteChapter(id);
    return ResponseEntity.noContent().build();
  }
}