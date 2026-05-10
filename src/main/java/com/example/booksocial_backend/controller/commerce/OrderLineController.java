package com.example.booksocial_backend.controller.commerce;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.commerce.OrderLineRequestDTO;
import com.example.booksocial_backend.DTO.commerce.OrderLineResponseDTO;
import com.example.booksocial_backend.service.OrderLineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de líneas de pedido.
 *
 * ✔ Usa DTOs (no expone entidades)
 * ✔ Orientado a API REST profesional
 *
 * @author Jorge
 * @since 2026
 */
@RestController
@RequestMapping("/api/order-lines")
@RequiredArgsConstructor
@Tag(name = "OrderLine Controller", description = "API REST para la gestión de líneas de pedido")
public class OrderLineController {

  private final OrderLineService orderLineService;

  // =========================
  // CREATE
  // =========================

  @Operation(summary = "Crear línea de pedido")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Línea creada correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<OrderLineResponseDTO> create(
      @Valid @RequestBody OrderLineRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(orderLineService.createOrderLine(request));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/batch")
  public ResponseEntity<List<OrderLineResponseDTO>> createMany(
      @Valid @RequestBody List<OrderLineRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista no puede estar vacía");
    }

    List<OrderLineResponseDTO> result = requests.stream()
        .map(orderLineService::createOrderLine)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  // =========================
  // READ
  // =========================

  @Operation(summary = "Obtener línea por ID")
  @GetMapping("/{id}")
  public ResponseEntity<OrderLineResponseDTO> getById(@PathVariable Long id) {

    return ResponseEntity.ok(orderLineService.getOrderLineById(id));
  }

  @Operation(summary = "Listar todas las líneas")
  @GetMapping
  public ResponseEntity<List<OrderLineResponseDTO>> getAll() {

    return ResponseEntity.ok(orderLineService.getAllOrderLines());
  }

  @Operation(summary = "Obtener líneas por pedido")
  @GetMapping("/order/{orderId}")
  public ResponseEntity<List<OrderLineResponseDTO>> getByOrder(@PathVariable Long orderId) {

    return ResponseEntity.ok(orderLineService.getOrderLinesByOrder(orderId));
  }

  // =========================
  // DELETE
  // =========================

  @Operation(summary = "Eliminar línea de pedido")
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {

    orderLineService.deleteOrderLine(id);
    return ResponseEntity.noContent().build();
  }
}