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

import com.example.booksocial_backend.DTO.commerce.TrackingOrderRequestDTO;
import com.example.booksocial_backend.DTO.commerce.TrackingOrderResponseDTO;
import com.example.booksocial_backend.service.TrackingOrderService;

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
@RequestMapping("/api/tracking-orders")
@RequiredArgsConstructor
@Tag(name = "Tracking Controller", description = "API REST para el seguimiento logístico de pedidos")
public class TrackingOrderController {

  private final TrackingOrderService trackingService;

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
  public ResponseEntity<TrackingOrderResponseDTO> create(
      @Valid @RequestBody TrackingOrderRequestDTO request) {

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
  public ResponseEntity<List<TrackingOrderResponseDTO>> createMany(
      @Valid @RequestBody List<TrackingOrderRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista de tracking no puede estar vacía");
    }

    List<TrackingOrderResponseDTO> result = requests.stream()
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
  public ResponseEntity<TrackingOrderResponseDTO> getById(
      @Parameter(description = "ID del tracking", example = "1") @PathVariable Long id) {

    return ResponseEntity.ok(trackingService.getTrackingById(id));
  }

  /**
   * Obtiene todos los registros de tracking.
   *
   * @return lista completa de tracking
   */
  @Operation(summary = "Obtener todos los tracking", description = "Devuelve todos los estados de tracking registrados en el sistema.")
  @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
  @GetMapping
  public ResponseEntity<List<TrackingOrderResponseDTO>> getAll() {

    return ResponseEntity.ok(trackingService.getAllTracking());
  }

  /**
   * Obtiene el último estado de tracking de un pedido.
   *
   * @param orderId ID del pedido
   * @return último estado de tracking
   */
  @Operation(summary = "Último estado del pedido", description = "Devuelve el estado más reciente del tracking de un pedido.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Estado obtenido correctamente"),
      @ApiResponse(responseCode = "404", description = "No hay tracking para este pedido")
  })
  @GetMapping("/order/{orderId}/latest")
  public ResponseEntity<TrackingOrderResponseDTO> getLatest(
      @Parameter(description = "ID del pedido", example = "1") @PathVariable Long orderId) {

    return ResponseEntity.ok(trackingService.getLatestTracking(orderId));
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
  public ResponseEntity<List<TrackingOrderResponseDTO>> getByOrder(
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
  public ResponseEntity<List<TrackingOrderResponseDTO>> getOrdered(
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