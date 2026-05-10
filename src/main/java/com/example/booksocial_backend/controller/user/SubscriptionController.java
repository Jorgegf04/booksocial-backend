package com.example.booksocial_backend.controller.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.user.SubscriptionRequestDTO;
import com.example.booksocial_backend.DTO.user.SubscriptionResponseDTO;
import com.example.booksocial_backend.service.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST encargado de la gestión de suscripciones dentro del sistema BookSocial.
 *
 * @author Jorge
 * @version 1.0
 * @since 2026
 */
@Tag(name = "Subscription Controller", description = "API REST para la gestión de suscripciones de usuarios")
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  // CREATE — requiere autenticación (solo el propio usuario o admin activa su suscripción)
  @Operation(summary = "Activar suscripción")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Suscripción activada correctamente"),
      @ApiResponse(responseCode = "400", description = "El usuario ya tiene una suscripción activa")
  })
  @PreAuthorize("isAuthenticated()")
  @PostMapping
  public ResponseEntity<SubscriptionResponseDTO> activate(@Valid @RequestBody SubscriptionRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.activateSubscription(request));
  }

  @Operation(summary = "Creación masiva de suscripciones")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/batch")
  public ResponseEntity<List<SubscriptionResponseDTO>> createMany(
      @Valid @RequestBody List<SubscriptionRequestDTO> requests) {
    if (requests == null || requests.isEmpty())
      throw new IllegalArgumentException("La lista de suscripciones no puede estar vacía");
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(requests.stream().map(subscriptionService::activateSubscription).toList());
  }

  // READ — requiere autenticación para consultar suscripción propia
  @Operation(summary = "Obtener suscripción por usuario")
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/user/{userId}")
  public ResponseEntity<SubscriptionResponseDTO> getByUserId(@PathVariable Long userId) {
    return ResponseEntity.ok(subscriptionService.getSubscriptionByUserId(userId));
  }

  // Comprobación pública (el frontend la usa para mostrar el estado sin autenticación)
  @Operation(summary = "Comprobar suscripción activa")
  @GetMapping("/user/{userId}/active")
  public ResponseEntity<Boolean> hasActive(@PathVariable Long userId) {
    return ResponseEntity.ok(subscriptionService.hasActiveSubscription(userId));
  }

  // DELETE — requiere autenticación
  @Operation(summary = "Cancelar suscripción")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Suscripción cancelada"),
      @ApiResponse(responseCode = "404", description = "Suscripción no encontrada")
  })
  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/user/{userId}")
  public ResponseEntity<Void> cancel(@PathVariable Long userId) {
    subscriptionService.cancelSubscription(userId);
    return ResponseEntity.noContent().build();
  }
}
