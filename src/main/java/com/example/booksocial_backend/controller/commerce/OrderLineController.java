package com.example.booksocial_backend.controller.commerce;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.booksocial_backend.domain.commerce.OrderLine;
import com.example.booksocial_backend.service.OrderLineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de líneas de pedido.
 *
 * <p>
 * Una línea de pedido representa un producto específico dentro de un pedido,
 * incluyendo la cantidad adquirida y el precio unitario en el momento de la
 * compra.
 * </p>
 *
 * <p>
 * Este controlador forma parte del sistema de compra y permite gestionar
 * las líneas de forma independiente con fines didácticos y de testing.
 * </p>
 *
 * <p>
 * <b>Responsabilidades:</b>
 * </p>
 * <ul>
 * <li>Creación de líneas de pedido</li>
 * <li>Creación masiva</li>
 * <li>Consulta individual y listados</li>
 * <li>Consulta por pedido</li>
 * <li>Eliminación</li>
 * </ul>
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

  /**
   * Crea una nueva línea de pedido.
   *
   * @param orderLine datos de la línea a crear
   * @return línea de pedido creada
   */
  @Operation(summary = "Crear línea de pedido", description = "Registra una nueva línea de pedido asociada a un pedido.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Línea creada correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping
  public ResponseEntity<OrderLine> create(@Valid @RequestBody OrderLine orderLine) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(orderLineService.createOrderLine(orderLine));
  }

  /**
   * Crea múltiples líneas de pedido.
   *
   * @param lines lista de líneas a crear
   * @return lista de líneas creadas
   */
  @Operation(summary = "Creación masiva de líneas de pedido", description = "Permite registrar múltiples líneas en una sola petición.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Líneas creadas correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista inválida"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping("/batch")
  public ResponseEntity<List<OrderLine>> createMany(@Valid @RequestBody List<OrderLine> lines) {

    if (lines == null || lines.isEmpty()) {
      throw new IllegalArgumentException("La lista de líneas no puede estar vacía");
    }

    List<OrderLine> result = lines.stream()
        .map(orderLineService::createOrderLine)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  // =========================
  // READ
  // =========================

  /**
   * Obtiene una línea de pedido por su ID.
   *
   * @param id ID de la línea
   * @return línea encontrada
   */
  @Operation(summary = "Obtener línea por ID", description = "Recupera una línea de pedido mediante su identificador.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Línea encontrada"),
      @ApiResponse(responseCode = "404", description = "Línea no encontrada")
  })
  @GetMapping("/{id}")
  public ResponseEntity<OrderLine> getById(@PathVariable Long id) {

    return ResponseEntity.ok(orderLineService.getOrderLineById(id));
  }

  /**
   * Obtiene todas las líneas de pedido.
   *
   * @return listado de líneas
   */
  @Operation(summary = "Listar líneas", description = "Devuelve todas las líneas de pedido del sistema.")
  @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
  @GetMapping
  public ResponseEntity<List<OrderLine>> getAll() {

    return ResponseEntity.ok(orderLineService.getAllOrderLines());
  }

  /**
   * Obtiene las líneas de un pedido concreto.
   *
   * @param orderId ID del pedido
   * @return lista de líneas del pedido
   */
  @Operation(summary = "Líneas por pedido", description = "Obtiene todas las líneas asociadas a un pedido.")
  @ApiResponse(responseCode = "200", description = "Líneas obtenidas correctamente")
  @GetMapping("/order/{orderId}")
  public ResponseEntity<List<OrderLine>> getByOrder(@PathVariable Long orderId) {

    return ResponseEntity.ok(orderLineService.getOrderLinesByOrder(orderId));
  }

  // =========================
  // DELETE
  // =========================

  /**
   * Elimina una línea de pedido.
   *
   * @param id ID de la línea
   */
  @Operation(summary = "Eliminar línea", description = "Elimina una línea de pedido del sistema.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Línea eliminada"),
      @ApiResponse(responseCode = "404", description = "Línea no encontrada")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {

    orderLineService.deleteOrderLine(id);
    return ResponseEntity.noContent().build();
  }
}