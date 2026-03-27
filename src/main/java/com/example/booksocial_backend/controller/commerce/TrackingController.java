package com.example.booksocial_backend.controller.commerce;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.booksocial_backend.DTO.commerce.TrackingRequestDTO;
import com.example.booksocial_backend.DTO.commerce.TrackingResponseDTO;
import com.example.booksocial_backend.service.TrackingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión del tracking de pedidos.
 *
 * <p>
 * Permite registrar y consultar el estado logístico de un pedido,
 * manteniendo un historial de cambios de estado.
 * </p>
 *
 * <p>
 * Este controlador forma parte del sistema de compra y permite
 * simular el seguimiento de envíos dentro de la plataforma BookSocial.
 * </p>
 *
 * <p>
 * <b>Responsabilidades:</b>
 * </p>
 * <ul>
 * <li>Registrar estados de tracking</li>
 * <li>Creación masiva de estados</li>
 * <li>Consulta individual</li>
 * <li>Consulta por pedido</li>
 * <li>Consulta ordenada cronológicamente</li>
 * <li>Eliminación de registros</li>
 * </ul>
 *
 * @author Jorge
 * @since 2026
 */
@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
@Tag(name = "Tracking Controller", description = "API REST para el seguimiento logístico de pedidos")
public class TrackingController {

  private final TrackingService trackingService;

  // =========================
  // CREATE
  // =========================

  /**
   * Añade un nuevo estado de tracking a un pedido.
   *
   * @param request datos del tracking
   * @return estado de tracking creado
   */
  @Operation(summary = "Añadir estado de tracking", description = "Registra un nuevo estado logístico para un pedido.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Estado añadido correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping
  public ResponseEntity<TrackingResponseDTO> create(
      @Valid @RequestBody TrackingRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(trackingService.addTracking(request));
  }

  /**
   * Añade múltiples estados de tracking en una sola petición.
   *
   * @param requests lista de estados
   * @return lista de estados creados
   */
  @Operation(summary = "Creación masiva de tracking", description = "Permite registrar múltiples estados logísticos.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Estados creados correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista inválida"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping("/batch")
  public ResponseEntity<List<TrackingResponseDTO>> createMany(
      @Valid @RequestBody List<TrackingRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista de tracking no puede estar vacía");
    }

    List<TrackingResponseDTO> result = requests.stream()
        .map(trackingService::addTracking)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  // =========================
  // READ
  // =========================

  /**
   * Obtiene un tracking por su ID.
   *
   * @param id ID del tracking
   * @return tracking encontrado
   */
  @Operation(summary = "Obtener tracking por ID", description = "Recupera un estado de tracking específico.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Tracking encontrado"),
      @ApiResponse(responseCode = "404", description = "Tracking no encontrado")
  })
  @GetMapping("/{id}")
  public ResponseEntity<TrackingResponseDTO> getById(
      @Parameter(description = "ID del tracking", example = "1") @PathVariable Long id) {

    return ResponseEntity.ok(trackingService.getTrackingById(id));
  }

  /**
   * Obtiene todos los estados de tracking de un pedido.
   *
   * @param orderId ID del pedido
   * @return lista de estados
   */
  @Operation(summary = "Tracking por pedido", description = "Obtiene todos los estados asociados a un pedido.")
  @ApiResponse(responseCode = "200", description = "Tracking obtenido correctamente")
  @GetMapping("/order/{orderId}")
  public ResponseEntity<List<TrackingResponseDTO>> getByOrder(
      @Parameter(description = "ID del pedido", example = "1") @PathVariable Long orderId) {

    return ResponseEntity.ok(trackingService.getTrackingByOrder(orderId));
  }

  /**
   * Obtiene el tracking de un pedido ordenado cronológicamente.
   *
   * @param orderId ID del pedido
   * @return lista ordenada de estados
   */
  @Operation(summary = "Tracking ordenado", description = "Devuelve el historial de tracking ordenado por fecha.")
  @ApiResponse(responseCode = "200", description = "Tracking ordenado correctamente")
  @GetMapping("/order/{orderId}/ordered")
  public ResponseEntity<List<TrackingResponseDTO>> getOrdered(
      @Parameter(description = "ID del pedido", example = "1") @PathVariable Long orderId) {

    return ResponseEntity.ok(trackingService.getTrackingByOrderOrdered(orderId));
  }

  // =========================
  // DELETE
  // =========================

  /**
   * Elimina un registro de tracking.
   *
   * @param id ID del tracking
   */
  @Operation(summary = "Eliminar tracking", description = "Elimina un estado de tracking del sistema.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Tracking eliminado"),
      @ApiResponse(responseCode = "404", description = "Tracking no encontrado")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "ID del tracking", example = "1") @PathVariable Long id) {

    trackingService.deleteTracking(id);
    return ResponseEntity.noContent().build();
  }
}