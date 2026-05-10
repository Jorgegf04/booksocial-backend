package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.TomeRequestDTO;
import com.example.booksocial_backend.DTO.catalog.TomeResponseDTO;
import com.example.booksocial_backend.service.TomeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de tomos dentro del sistema BookSocial.
 *
 * <p>
 * La entidad Tome representa una unidad estructural dentro de una edición,
 * especialmente en obras tipo manga, donde los capítulos se agrupan en tomos.
 * </p>
 *
 * <p>
 * Este controlador forma parte del modelo de obras complejas del catálogo,
 * permitiendo gestionar la organización jerárquica de contenido editorial.
 * </p>
 *
 * <p>
 * <b>Responsabilidades:</b>
 * </p>
 * <ul>
 * <li>Crear tomos individuales o en lote</li>
 * <li>Consultar tomos por ID o edición</li>
 * <li>Obtener tomos ordenados (importante para lectura secuencial)</li>
 * <li>Actualizar tomos</li>
 * <li>Eliminar tomos</li>
 * </ul>
 *
 * <p>
 * Todos los endpoints siguen principios REST y devuelven datos en formato JSON.
 * </p>
 *
 * @author Jorge
 * @version 1.1
 * @since 2026
 */
@RestController
@RequestMapping("/api/tomes")
@RequiredArgsConstructor
@Tag(name = "Tome Controller", description = "API REST para la gestión de tomos dentro de ediciones (estructura de mangas)")
public class TomeController {

  private final TomeService tomeService;

  // =========================
  // CREATE
  // =========================

  /**
   * Crea un nuevo tomo.
   *
   * @param request datos del tomo
   * @return tomo creado
   */
  @Operation(summary = "Crear tomo", description = "Registra un nuevo tomo asociado a una edición.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Tomo creado correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<TomeResponseDTO> create(
      @Valid @RequestBody TomeRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(tomeService.createTome(request));
  }

  /**
   * Crea múltiples tomos en una sola petición.
   *
   * @param requests lista de tomos
   * @return lista de tomos creados
   */
  @Operation(summary = "Creación masiva de tomos", description = "Permite registrar múltiples tomos asociados a una edición.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Tomos creados correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista vacía o inválida"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/batch")
  public ResponseEntity<List<TomeResponseDTO>> createMany(
      @Valid @RequestBody List<TomeRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista de tomos no puede estar vacía");
    }

    List<TomeResponseDTO> result = requests.stream()
        .map(tomeService::createTome)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  // =========================
  // READ
  // =========================

  /**
   * Obtiene un tomo por su ID.
   */
  @Operation(summary = "Obtener tomo por ID", description = "Recupera un tomo específico mediante su identificador.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Tomo encontrado"),
      @ApiResponse(responseCode = "404", description = "Tomo no encontrado")
  })
  @GetMapping("/{id}")
  public ResponseEntity<TomeResponseDTO> getById(
      @Parameter(description = "ID del tomo", example = "1") @PathVariable Long id) {

    return ResponseEntity.ok(tomeService.getTomeById(id));
  }

  /**
   * Obtiene todos los tomos.
   */
  @Operation(summary = "Listar tomos", description = "Devuelve todos los tomos registrados en el sistema.")
  @GetMapping
  public ResponseEntity<List<TomeResponseDTO>> getAll() {

    return ResponseEntity.ok(tomeService.getAllTomes());
  }

  /**
   * Obtiene los tomos asociados a una edición.
   */
  @Operation(summary = "Tomos por edición", description = "Devuelve todos los tomos asociados a una edición concreta.")
  @GetMapping("/edition/{editionId}")
  public ResponseEntity<List<TomeResponseDTO>> getByEdition(
      @Parameter(description = "ID de la edición", example = "1") @PathVariable Long editionId) {

    return ResponseEntity.ok(tomeService.getTomesByEdition(editionId));
  }

  /**
   * Obtiene los tomos de una edición ordenados.
   *
   * <p>
   * Este endpoint es clave para mantener el orden de lectura correcto
   * dentro de obras secuenciales como mangas.
   * </p>
   */
  @Operation(summary = "Tomos ordenados por edición", description = "Devuelve los tomos ordenados secuencialmente dentro de una edición.")
  @GetMapping("/edition/{editionId}/ordered")
  public ResponseEntity<List<TomeResponseDTO>> getOrdered(
      @Parameter(description = "ID de la edición", example = "1") @PathVariable Long editionId) {

    return ResponseEntity.ok(tomeService.getTomesByEditionOrdered(editionId));
  }

  // =========================
  // UPDATE
  // =========================

  /**
   * Actualiza un tomo existente.
   */
  @Operation(summary = "Actualizar tomo", description = "Modifica los datos de un tomo existente.")
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<TomeResponseDTO> update(
      @Parameter(description = "ID del tomo", example = "1") @PathVariable Long id,
      @Valid @RequestBody TomeRequestDTO request) {

    return ResponseEntity.ok(tomeService.updateTome(id, request));
  }

  // =========================
  // DELETE
  // =========================

  /**
   * Elimina un tomo del sistema.
   */
  @Operation(summary = "Eliminar tomo", description = "Elimina permanentemente un tomo del sistema.")
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "ID del tomo", example = "1") @PathVariable Long id) {

    tomeService.deleteTome(id);
    return ResponseEntity.noContent().build();
  }
}