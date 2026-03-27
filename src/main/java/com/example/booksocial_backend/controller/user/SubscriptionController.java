package com.example.booksocial_backend.controller.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.user.SubscriptionRequestDTO;
import com.example.booksocial_backend.DTO.user.SubscriptionResponseDTO;
import com.example.booksocial_backend.service.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST encargado de la gestión de suscripciones dentro del sistema
 * BookSocial.
 *
 * <p>
 * La entidad Subscription representa el plan mensual asociado a un usuario,
 * permitiéndole acceder a funcionalidades avanzadas como descuentos, eventos
 * exclusivos
 * y estadísticas ampliadas.
 * </p>
 *
 * <p>
 * Este controlador expone los endpoints necesarios para gestionar la
 * activación,
 * cancelación y consulta del estado de las suscripciones.
 * </p>
 *
 * <p>
 * <b>Responsabilidades principales:</b>
 * </p>
 * <ul>
 * <li>Activar suscripción</li>
 * <li>Cancelar suscripción</li>
 * <li>Consultar estado de suscripción</li>
 * <li>Verificar si un usuario está suscrito</li>
 * </ul>
 *
 * <p>
 * Todas las respuestas se devuelven en formato JSON y están diseñadas para ser
 * consumidas
 * por clientes REST (frontend, Postman, Swagger UI, etc.).
 * </p>
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

  // =========================
  // CREATE
  // =========================

  /**
   * Activa una suscripción para un usuario.
   *
   * @param request DTO con el ID del usuario
   * @return suscripción creada
   */
  @Operation(summary = "Activar suscripción", description = "Activa una suscripción mensual para un usuario.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Suscripción activada correctamente"),
      @ApiResponse(responseCode = "400", description = "El usuario ya tiene una suscripción activa"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping
  public ResponseEntity<SubscriptionResponseDTO> activate(
      @Valid @RequestBody SubscriptionRequestDTO request) {

    SubscriptionResponseDTO response = subscriptionService.activateSubscription(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * Activación masiva de suscripciones.
   *
   * <p>
   * Permite registrar múltiples suscripciones en una sola petición.
   * Ideal para carga inicial de datos o testing.
   * </p>
   */
  @Operation(summary = "Creación masiva de suscripciones", description = "Permite activar múltiples suscripciones en una sola petición.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Suscripciones creadas correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista vacía o datos inválidos")
  })
  @PostMapping("/batch")
  public ResponseEntity<List<SubscriptionResponseDTO>> createMany(
      @Valid @RequestBody List<SubscriptionRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista de suscripciones no puede estar vacía");
    }

    List<SubscriptionResponseDTO> responses = requests.stream()
        .map(subscriptionService::activateSubscription)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(responses);
  }

  // =========================
  // READ
  // =========================

  /**
   * Obtiene la suscripción de un usuario.
   *
   * @param userId ID del usuario
   * @return suscripción asociada
   */
  @Operation(summary = "Obtener suscripción por usuario", description = "Recupera la suscripción de un usuario específico.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Suscripción encontrada"),
      @ApiResponse(responseCode = "404", description = "Suscripción no encontrada")
  })
  @GetMapping("/user/{userId}")
  public ResponseEntity<SubscriptionResponseDTO> getByUserId(
      @Parameter(description = "ID del usuario", example = "1") @PathVariable Long userId) {

    return ResponseEntity.ok(subscriptionService.getSubscriptionByUserId(userId));
  }

  /**
   * Comprueba si un usuario tiene suscripción activa.
   *
   * @param userId ID del usuario
   * @return true/false
   */
  @Operation(summary = "Comprobar suscripción activa", description = "Indica si un usuario tiene una suscripción activa.")
  @GetMapping("/user/{userId}/active")
  public ResponseEntity<Boolean> hasActive(
      @Parameter(description = "ID del usuario", example = "1") @PathVariable Long userId) {

    return ResponseEntity.ok(subscriptionService.hasActiveSubscription(userId));
  }

  // =========================
  // DELETE
  // =========================

  /**
   * Cancela la suscripción de un usuario.
   *
   * @param userId ID del usuario
   */
  @Operation(summary = "Cancelar suscripción", description = "Desactiva la suscripción de un usuario.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Suscripción cancelada"),
      @ApiResponse(responseCode = "404", description = "Suscripción no encontrada")
  })
  @DeleteMapping("/user/{userId}")
  public ResponseEntity<Void> cancel(
      @Parameter(description = "ID del usuario", example = "1") @PathVariable Long userId) {

    subscriptionService.cancelSubscription(userId);
    return ResponseEntity.noContent().build();
  }
}