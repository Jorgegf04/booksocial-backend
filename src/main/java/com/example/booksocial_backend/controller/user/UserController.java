package com.example.booksocial_backend.controller.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.social.TrackingWorkResponseDTO;
import com.example.booksocial_backend.DTO.user.*;
import com.example.booksocial_backend.domain.social.TrackingWork;
import com.example.booksocial_backend.domain.user.Role;
import com.example.booksocial_backend.service.TrackingWorkService;
import com.example.booksocial_backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de usuarios.
 *
 * Permite realizar operaciones CRUD y consultas avanzadas
 * sobre los usuarios del sistema BookSocial.
 * 
 * @author Jorge
 * @version 1.0
 * @since 26/03/2026
 */
@Tag(name = "User Controller", description = "Gestión completa de usuarios del sistema BookSocial")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final TrackingWorkService trackingWorkService;

  // =========================
  // CREATE
  // =========================

  @Operation(summary = "Crear usuario", description = "Registra un nuevo usuario en el sistema.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "500", description = "Error interno")
  })
  @PostMapping
  public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequestDTO request) {
    UserResponseDTO user = userService.createUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @Operation(summary = "Crear múltiples usuarios", description = "Permite registrar varios usuarios en una sola petición.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Usuarios creados correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista vacía o inválida")
  })
  @PostMapping("/batch")
  public ResponseEntity<List<UserResponseDTO>> createMany(
      @RequestBody List<CreateUserRequestDTO> requests) {

    List<UserResponseDTO> users = requests.stream()
        .map(userService::createUser)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(users);
  }

  // =========================
  // READ
  // =========================

  @Operation(summary = "Obtener usuario por ID", description = "Recupera un usuario específico mediante su identificador.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @Operation(summary = "Obtener todos los usuarios", description = "Devuelve la lista completa de usuarios.")
  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @Operation(summary = "Obtener usuarios activos", description = "Devuelve únicamente los usuarios activos.")
  @GetMapping("/active")
  public ResponseEntity<List<UserResponseDTO>> getActiveUsers() {
    return ResponseEntity.ok(userService.getActiveUsers());
  }

  @Operation(summary = "Buscar usuarios por rol", description = "Filtra usuarios según su rol en el sistema.")
  @GetMapping("/role/{role}")
  public ResponseEntity<List<UserResponseDTO>> getByRole(@PathVariable Role role) {
    return ResponseEntity.ok(userService.getUsersByRole(role));
  }

  // =========================
  // UPDATE
  // =========================

  @Operation(summary = "Actualizar usuario", description = "Permite modificar los datos de un usuario existente.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> updateUser(
      @PathVariable Long id,
      @RequestBody UpdateUserRequestDTO request) {

    return ResponseEntity.ok(userService.updateUser(id, request));
  }

  // =========================
  // DELETE
  // =========================

  @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema. Operación irreversible.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  // =========================
  // STATUS
  // =========================

  @Operation(summary = "Activar o desactivar usuario", description = "Permite cambiar el estado activo de un usuario.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Estado actualizado"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @PatchMapping("/{id}/active")
  public ResponseEntity<UserResponseDTO> setActive(
      @PathVariable Long id,
      @RequestParam Boolean active) {

    return ResponseEntity.ok(userService.setUserActive(id, active));
  }

  /**
   * Obtiene las obras seguidas por un usuario.
   *
   * <p>
   * Devuelve la lista de seguimientos del usuario, incluyendo
   * el estado de cada obra (pendiente, leyendo, completada, etc).
   * </p>
   *
   * @param id ID del usuario
   * @return lista de obras seguidas
   */
  @Operation(summary = "Obras seguidas por usuario", description = "Devuelve todas las obras que sigue un usuario con su estado.")
  @GetMapping("/{id}/tracking")
  public ResponseEntity<List<TrackingWorkResponseDTO>> getTrackingByUser(
      @PathVariable Long id) {

    return ResponseEntity.ok(trackingWorkService.getByUser(id));
  }

  // =========================
  // FOLLOW SYSTEM
  // =========================

  /**
   * Permite a un usuario seguir a otro usuario.
   *
   * <p>
   * Establece una relación de seguimiento entre dos usuarios,
   * donde el usuario origen (follower) comienza a seguir
   * al usuario destino (following).
   * </p>
   *
   * @param id       ID del usuario que sigue
   * @param targetId ID del usuario a seguir
   * @return mensaje de confirmación
   */
  @Operation(summary = "Seguir usuario", description = "Permite a un usuario seguir a otro usuario.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Usuario seguido correctamente"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
      @ApiResponse(responseCode = "400", description = "Relación inválida")
  })
  @PostMapping("/{id}/follow/{targetId}")
  public ResponseEntity<String> followUser(
      @PathVariable Long id,
      @PathVariable Long targetId) {

    userService.followUser(id, targetId);
    return ResponseEntity.ok("Usuario seguido correctamente");
  }

  /**
   * Permite a un usuario dejar de seguir a otro usuario.
   *
   * <p>
   * Elimina la relación de seguimiento existente entre dos usuarios.
   * </p>
   *
   * @param id       ID del usuario que deja de seguir
   * @param targetId ID del usuario seguido
   * @return mensaje de confirmación
   */
  @Operation(summary = "Dejar de seguir usuario", description = "Permite eliminar una relación de seguimiento entre usuarios.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Dejó de seguir correctamente"),
      @ApiResponse(responseCode = "404", description = "Relación no encontrada")
  })
  @DeleteMapping("/{id}/follow/{targetId}")
  public ResponseEntity<String> unfollowUser(
      @PathVariable Long id,
      @PathVariable Long targetId) {

    userService.unfollowUser(id, targetId);
    return ResponseEntity.ok("Dejó de seguir correctamente");
  }

  /**
   * Obtiene la lista de seguidores de un usuario.
   *
   * <p>
   * Devuelve todos los usuarios que siguen al usuario indicado.
   * </p>
   *
   * @param id ID del usuario
   * @return lista de seguidores
   */
  @Operation(summary = "Obtener seguidores", description = "Devuelve la lista de usuarios que siguen a un usuario.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Lista de seguidores"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @GetMapping("/{id}/followers")
  public ResponseEntity<List<UserResponseDTO>> getFollowers(
      @PathVariable Long id) {

    return ResponseEntity.ok(userService.getFollowers(id));
  }

  /**
   * Obtiene la lista de usuarios seguidos por un usuario.
   *
   * <p>
   * Devuelve todos los usuarios a los que sigue el usuario indicado.
   * </p>
   *
   * @param id ID del usuario
   * @return lista de usuarios seguidos
   */
  @Operation(summary = "Obtener seguidos", description = "Devuelve la lista de usuarios que sigue un usuario.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Lista de seguidos"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @GetMapping("/{id}/following")
  public ResponseEntity<List<UserResponseDTO>> getFollowing(
      @PathVariable Long id) {

    return ResponseEntity.ok(userService.getFollowing(id));
  }
}