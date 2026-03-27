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

import com.example.booksocial_backend.DTO.commerce.OrderRequestDTO;
import com.example.booksocial_backend.DTO.commerce.OrderResponseDTO;
import com.example.booksocial_backend.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST encargado de la gestión de pedidos dentro del sistema
 * BookSocial.
 *
 * <p>
 * Un pedido representa una compra realizada por un usuario e incluye múltiples
 * líneas de pedido que contienen los productos adquiridos.
 * </p>
 *
 * <p>
 * Este controlador forma parte del subsistema de compra, permitiendo gestionar
 * el ciclo completo de los pedidos mediante una API REST.
 * </p>
 *
 * <p>
 * <b>Responsabilidades principales:</b>
 * </p>
 * <ul>
 * <li>Creación de pedidos</li>
 * <li>Creación masiva de pedidos</li>
 * <li>Consulta de pedidos</li>
 * <li>Consulta por usuario</li>
 * <li>Eliminación de pedidos</li>
 * </ul>
 *
 * <p>
 * Todas las respuestas se devuelven en formato JSON y están diseñadas para ser
 * consumidas por clientes REST como Postman, Swagger o frontend.
 * </p>
 *
 * @author Jorge
 * @version 1.1
 * @since 2026
 */
@Tag(name = "Order Controller", description = "API REST para la gestión de pedidos dentro del sistema de compra")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  // =========================
  // CREATE
  // =========================

  /**
   * Crea un nuevo pedido en el sistema.
   *
   * @param request DTO con la información del pedido
   * @return Pedido creado con sus datos completos
   */
  @Operation(summary = "Crear pedido", description = "Registra un nuevo pedido con sus líneas asociadas.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Pedido creado correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping
  public ResponseEntity<OrderResponseDTO> createOrder(
      @Valid @RequestBody OrderRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(orderService.createOrder(request));
  }

  /**
   * Crea múltiples pedidos en una única operación.
   *
   * @param requests lista de pedidos a registrar
   * @return lista de pedidos creados
   */
  @Operation(summary = "Creación masiva de pedidos", description = "Permite registrar múltiples pedidos en una sola petición.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Pedidos creados correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista vacía o inválida"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping("/batch")
  public ResponseEntity<List<OrderResponseDTO>> createMany(
      @Valid @RequestBody List<OrderRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista de pedidos no puede estar vacía");
    }

    List<OrderResponseDTO> result = requests.stream()
        .map(orderService::createOrder)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  // =========================
  // READ
  // =========================

  /**
   * Obtiene un pedido por su identificador.
   *
   * @param id ID del pedido
   * @return Pedido encontrado
   */
  @Operation(summary = "Obtener pedido por ID", description = "Recupera un pedido específico mediante su identificador.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
      @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
  })
  @GetMapping("/{id}")
  public ResponseEntity<OrderResponseDTO> getById(
      @Parameter(description = "ID del pedido", example = "1") @PathVariable Long id) {

    return ResponseEntity.ok(orderService.getOrderById(id));
  }

  /**
   * Obtiene todos los pedidos del sistema.
   *
   * @return listado de pedidos
   */
  @Operation(summary = "Listar pedidos", description = "Devuelve todos los pedidos registrados en el sistema.")
  @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
  @GetMapping
  public ResponseEntity<List<OrderResponseDTO>> getAll() {

    return ResponseEntity.ok(orderService.getAllOrders());
  }

  /**
   * Obtiene los pedidos de un usuario.
   *
   * @param userId ID del usuario
   * @return lista de pedidos del usuario
   */
  @Operation(summary = "Pedidos por usuario", description = "Obtiene todos los pedidos realizados por un usuario.")
  @ApiResponse(responseCode = "200", description = "Pedidos obtenidos correctamente")
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<OrderResponseDTO>> getByUser(
      @Parameter(description = "ID del usuario", example = "1") @PathVariable Long userId) {

    return ResponseEntity.ok(orderService.getOrdersByUser(userId));
  }

  // =========================
  // DELETE
  // =========================

  /**
   * Elimina un pedido del sistema.
   *
   * @param id ID del pedido
   * @return respuesta sin contenido
   */
  @Operation(summary = "Eliminar pedido", description = "Elimina un pedido del sistema.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Pedido eliminado"),
      @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "ID del pedido", example = "1") @PathVariable Long id) {

    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }
}