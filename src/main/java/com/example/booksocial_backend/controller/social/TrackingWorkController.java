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
/**
 * Controlador REST para la gestión del seguimiento de obras.
 *
 * <p>
 * Este controlador permite a los usuarios interactuar con el sistema social
 * de BookSocial mediante el seguimiento de obras.
 * </p>
 *
 * <p>
 * Funcionalidades principales:
 * </p>
 * <ul>
 * <li>Crear seguimiento de obra</li>
 * <li>Crear múltiples seguimientos</li>
 * <li>Consultar obras seguidas por usuario</li>
 * <li>Consultar usuarios que siguen una obra</li>
 * <li>Eliminar seguimiento</li>
 * </ul>
 *
 * @author Jorge
 * @since 2026
 */
@RestController
@RequestMapping("/api/tracking-works")
@RequiredArgsConstructor
@Tag(name = "TrackingWork Controller", description = "API REST para gestión del seguimiento de obras")
public class TrackingWorkController {

  private final TrackingWorkService service;

  // =========================
  // CREATE
  // =========================

  /**
   * Permite a un usuario seguir una obra.
   *
   * @param request DTO con userId, workId y estado opcional
   * @return seguimiento creado
   */
  @Operation(summary = "Seguir obra", description = "Crea un nuevo seguimiento entre un usuario y una obra.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Seguimiento creado correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos o duplicados"),
      @ApiResponse(responseCode = "404", description = "Obra no encontrada")
  })
  @PostMapping
  public ResponseEntity<TrackingWorkResponseDTO> create(
      @RequestBody TrackingWorkRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.create(request));
  }

  /**
   * Permite seguir múltiples obras en una sola petición.
   *
   * @param requests lista de solicitudes
   * @return lista de seguimientos creados
   */
  @Operation(summary = "Seguimiento masivo", description = "Permite crear múltiples relaciones de seguimiento en una única petición.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Seguimientos creados correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista vacía o inválida")
  })
  @PostMapping("/batch")
  public ResponseEntity<List<TrackingWorkResponseDTO>> createMany(
      @RequestBody List<TrackingWorkRequestDTO> requests) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.createMany(requests));
  }

  // =========================
  // READ
  // =========================

  /**
   * Obtiene todas las obras que sigue un usuario.
   *
   * @param userId ID del usuario
   * @return lista de seguimientos
   */
  @Operation(summary = "Obras seguidas por usuario", description = "Devuelve todas las obras que sigue un usuario con su estado.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Lista de seguimientos"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<TrackingWorkResponseDTO>> getByUser(
      @PathVariable Long userId) {

    return ResponseEntity.ok(service.getByUser(userId));
  }

  /**
   * Obtiene todos los usuarios que siguen una obra.
   *
   * @param workId ID de la obra
   * @return lista de usuarios seguidores
   */
  @Operation(summary = "Usuarios que siguen una obra", description = "Devuelve todos los usuarios que siguen una obra específica.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Lista de seguidores"),
      @ApiResponse(responseCode = "404", description = "Obra no encontrada")
  })
  @GetMapping("/work/{workId}")
  public ResponseEntity<List<TrackingWorkResponseDTO>> getByWork(
      @PathVariable Long workId) {

    return ResponseEntity.ok(service.getByWork(workId));
  }

  // =========================
  // UPDATE
  // =========================

  /**
   * Actualiza el estado de un seguimiento.
   *
   * <p>
   * Permite modificar el estado de una obra dentro del seguimiento
   * de un usuario (PENDING, READING, COMPLETED, etc).
   * </p>
   *
   * @param id      ID del seguimiento
   * @param request DTO con el nuevo estado
   * @return seguimiento actualizado
   */
  @Operation(summary = "Actualizar estado de seguimiento", description = "Permite cambiar el estado de una obra dentro del seguimiento de un usuario.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Seguimiento actualizado correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "404", description = "Seguimiento no encontrado")
  })
  @PutMapping("/{id}")
  public ResponseEntity<TrackingWorkResponseDTO> updateStatus(
      @PathVariable Long id,
      @RequestBody TrackingWorkRequestDTO request) {

    return ResponseEntity.ok(service.updateStatus(id, request));
  }

  // =========================
  // DELETE
  // =========================

  /**
   * Elimina un seguimiento por su identificador.
   *
   * @param id ID del seguimiento
   */
  @Operation(summary = "Eliminar seguimiento", description = "Elimina un seguimiento de obra existente mediante su ID.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Seguimiento eliminado correctamente"),
      @ApiResponse(responseCode = "404", description = "Seguimiento no encontrado")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {

    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}