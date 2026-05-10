package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.VolumeRequestDTO;
import com.example.booksocial_backend.DTO.catalog.VolumeResponseDTO;
import com.example.booksocial_backend.service.VolumeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de volúmenes dentro del sistema BookSocial.
 *
 * <p>
 * La entidad Volume representa una unidad estructural dentro de una edición,
 * utilizada principalmente en cómics y colecciones editoriales.
 * </p>
 *
 * <p>
 * Este controlador forma parte del modelo de obras complejas del catálogo,
 * permitiendo gestionar la organización de volúmenes asociados a ediciones.
 * </p>
 *
 * <p>
 * <b>Responsabilidades:</b>
 * </p>
 * <ul>
 * <li>Crear volúmenes individuales o en lote</li>
 * <li>Consultar volúmenes por ID o edición</li>
 * <li>Obtener volúmenes ordenados</li>
 * <li>Actualizar volúmenes</li>
 * <li>Eliminar volúmenes</li>
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
@RequestMapping("/api/volumes")
@RequiredArgsConstructor
@Tag(name = "Volume Controller", description = "API REST para la gestión de volúmenes (cómics y colecciones)")
public class VolumeController {

  private final VolumeService volumeService;

  // =========================
  // CREATE
  // =========================

  /**
   * Crea un nuevo volumen.
   *
   * @param request datos del volumen
   * @return volumen creado
   */
  @Operation(summary = "Crear volumen", description = "Registra un nuevo volumen asociado a una edición.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Volumen creado correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<VolumeResponseDTO> create(
      @Valid @RequestBody VolumeRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(volumeService.createVolume(request));
  }

  /**
   * Crea múltiples volúmenes en una única petición.
   *
   * @param requests lista de volúmenes
   * @return lista de volúmenes creados
   */
  @Operation(summary = "Creación masiva de volúmenes", description = "Permite registrar múltiples volúmenes asociados a distintas ediciones.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Volúmenes creados correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista vacía o inválida"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/batch")
  public ResponseEntity<List<VolumeResponseDTO>> createMany(
      @Valid @RequestBody List<VolumeRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista de volúmenes no puede estar vacía");
    }

    List<VolumeResponseDTO> result = requests.stream()
        .map(volumeService::createVolume)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  // =========================
  // READ
  // =========================

  /**
   * Obtiene un volumen por su ID.
   */
  @Operation(summary = "Obtener volumen por ID", description = "Recupera un volumen específico mediante su identificador.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Volumen encontrado"),
      @ApiResponse(responseCode = "404", description = "Volumen no encontrado")
  })
  @GetMapping("/{id}")
  public ResponseEntity<VolumeResponseDTO> getById(
      @Parameter(description = "ID del volumen", example = "1") @PathVariable Long id) {

    return ResponseEntity.ok(volumeService.getVolumeById(id));
  }

  /**
   * Obtiene todos los volúmenes.
   */
  @Operation(summary = "Listar volúmenes", description = "Devuelve todos los volúmenes registrados en el sistema.")
  @GetMapping
  public ResponseEntity<List<VolumeResponseDTO>> getAll() {

    return ResponseEntity.ok(volumeService.getAllVolumes());
  }

  /**
   * Obtiene los volúmenes asociados a una edición.
   */
  @Operation(summary = "Volúmenes por edición", description = "Devuelve todos los volúmenes asociados a una edición concreta.")
  @GetMapping("/edition/{editionId}")
  public ResponseEntity<List<VolumeResponseDTO>> getByEdition(
      @Parameter(description = "ID de la edición", example = "1") @PathVariable Long editionId) {

    return ResponseEntity.ok(volumeService.getVolumesByEdition(editionId));
  }

  /**
   * Obtiene los volúmenes ordenados de una edición.
   *
   * <p>
   * Fundamental para mantener el orden de lectura en colecciones secuenciales.
   * </p>
   */
  @Operation(summary = "Volúmenes ordenados por edición", description = "Devuelve los volúmenes ordenados secuencialmente dentro de una edición.")
  @GetMapping("/edition/{editionId}/ordered")
  public ResponseEntity<List<VolumeResponseDTO>> getOrdered(
      @Parameter(description = "ID de la edición", example = "1") @PathVariable Long editionId) {

    return ResponseEntity.ok(volumeService.getVolumesByEditionOrdered(editionId));
  }

  // =========================
  // UPDATE
  // =========================

  /**
   * Actualiza un volumen existente.
   */
  @Operation(summary = "Actualizar volumen", description = "Modifica los datos de un volumen existente.")
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<VolumeResponseDTO> update(
      @Parameter(description = "ID del volumen", example = "1") @PathVariable Long id,
      @Valid @RequestBody VolumeRequestDTO request) {

    return ResponseEntity.ok(volumeService.updateVolume(id, request));
  }

  // =========================
  // DELETE
  // =========================

  /**
   * Elimina un volumen del sistema.
   */
  @Operation(summary = "Eliminar volumen", description = "Elimina permanentemente un volumen del sistema.")
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "ID del volumen", example = "1") @PathVariable Long id) {

    volumeService.deleteVolume(id);
    return ResponseEntity.noContent().build();
  }
}