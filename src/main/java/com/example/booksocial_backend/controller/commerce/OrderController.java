package com.example.booksocial_backend.controller.commerce;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
 * Controlador REST encargado de la gestión de pedidos dentro del sistema BookSocial.
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

  // CREATE — público para permitir checkout como invitado (sin cuenta)
  @Operation(summary = "Crear pedido", description = "Registra un nuevo pedido. Accesible sin cuenta (checkout de invitado).")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Pedido creado correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos")
  })
  @PostMapping
  public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
  }

  @Operation(summary = "Creación masiva de pedidos")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/batch")
  public ResponseEntity<List<OrderResponseDTO>> createMany(@Valid @RequestBody List<OrderRequestDTO> requests) {
    if (requests == null || requests.isEmpty())
      throw new IllegalArgumentException("La lista de pedidos no puede estar vacía");
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(requests.stream().map(orderService::createOrder).toList());
  }

  // READ — listado global solo ADMIN; por ID o usuario requiere autenticación
  @Operation(summary = "Listar pedidos")
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<OrderResponseDTO>> getAll() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @Operation(summary = "Obtener pedido por ID")
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/{id}")
  public ResponseEntity<OrderResponseDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.getOrderById(id));
  }

  @Operation(summary = "Pedidos por usuario")
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<OrderResponseDTO>> getByUser(@PathVariable Long userId) {
    return ResponseEntity.ok(orderService.getOrdersByUser(userId));
  }

  // DELETE — solo ADMIN
  @Operation(summary = "Eliminar pedido")
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }
}
