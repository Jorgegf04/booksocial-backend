package com.example.booksocial_backend.controller.social;

import java.util.List;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.social.*;
import com.example.booksocial_backend.service.TrackingWorkService;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión del seguimiento de obras.
 *
 * <p>
 * Permite a los usuarios seguir obras dentro del sistema,
 * facilitando la interacción social y la personalización del contenido.
 * </p>
 *
 * <p>
 * Funcionalidades:
 * </p>
 * <ul>
 * <li>Seguir obra</li>
 * <li>Seguimiento masivo</li>
 * <li>Consultar obras seguidas</li>
 * <li>Consultar seguidores de una obra</li>
 * <li>Eliminar seguimiento</li>
 * </ul>
 *
 * @author Jorge
 * @since 2026
 */
@RestController
@RequestMapping("/api/tracking-works")
@RequiredArgsConstructor
@Tag(name = "TrackingWork Controller", description = "API REST para seguimiento de obras")
public class TrackingWorkController {

  private final TrackingWorkService service;

  // =========================
  // CREATE
  // =========================

  @Operation(summary = "Seguir obra", description = "Permite a un usuario seguir una obra.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Seguimiento creado correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos")
  })
  @PostMapping
  public ResponseEntity<TrackingWorkResponseDTO> create(
      @RequestBody TrackingWorkRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.create(request));
  }

  @Operation(summary = "Seguimiento masivo", description = "Permite seguir múltiples obras en una sola petición.")
  @PostMapping("/batch")
  public ResponseEntity<List<TrackingWorkResponseDTO>> createMany(
      @RequestBody List<TrackingWorkRequestDTO> requests) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.createMany(requests));
  }

  // =========================
  // READ
  // =========================

  @Operation(summary = "Obras seguidas por usuario")
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<TrackingWorkResponseDTO>> getByUser(
      @PathVariable Long userId) {

    return ResponseEntity.ok(service.getByUser(userId));
  }

  @Operation(summary = "Usuarios que siguen una obra")
  @GetMapping("/work/{workId}")
  public ResponseEntity<List<TrackingWorkResponseDTO>> getByWork(
      @PathVariable Long workId) {

    return ResponseEntity.ok(service.getByWork(workId));
  }

  // =========================
  // DELETE
  // =========================

  @Operation(summary = "Eliminar seguimiento")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {

    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}