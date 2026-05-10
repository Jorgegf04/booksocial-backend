package com.example.booksocial_backend.controller.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.social.TrackingWorkResponseDTO;
import com.example.booksocial_backend.DTO.user.*;
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

  // CREATE — solo ADMIN (el registro público va por /api/auth/register)
  @Operation(summary = "Crear usuario")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
  }

  @Operation(summary = "Crear múltiples usuarios")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/batch")
  public ResponseEntity<List<UserResponseDTO>> createMany(@RequestBody List<CreateUserRequestDTO> requests) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(requests.stream().map(userService::createUser).toList());
  }

  // READ — perfil individual público; listados solo ADMIN
  @Operation(summary = "Obtener usuario por ID")
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @Operation(summary = "Obtener todos los usuarios")
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @Operation(summary = "Obtener usuarios activos")
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/active")
  public ResponseEntity<List<UserResponseDTO>> getActiveUsers() {
    return ResponseEntity.ok(userService.getActiveUsers());
  }

  @Operation(summary = "Buscar usuarios por rol")
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/role/{role}")
  public ResponseEntity<List<UserResponseDTO>> getByRole(@PathVariable Role role) {
    return ResponseEntity.ok(userService.getUsersByRole(role));
  }

  // UPDATE — cualquier usuario autenticado (el servicio debería validar que edita su propio perfil)
  @Operation(summary = "Actualizar usuario")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @PreAuthorize("isAuthenticated()")
  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
      @RequestBody UpdateUserRequestDTO request) {
    return ResponseEntity.ok(userService.updateUser(id, request));
  }

  // DELETE / STATUS — solo ADMIN
  @Operation(summary = "Eliminar usuario")
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Activar o desactivar usuario")
  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}/active")
  public ResponseEntity<UserResponseDTO> setActive(@PathVariable Long id, @RequestParam Boolean active) {
    return ResponseEntity.ok(userService.setUserActive(id, active));
  }

  // TRACKING — público (estadísticas de seguimiento visibles por todos)
  @Operation(summary = "Obras seguidas por usuario")
  @GetMapping("/{id}/tracking")
  public ResponseEntity<List<TrackingWorkResponseDTO>> getTrackingByUser(@PathVariable Long id) {
    return ResponseEntity.ok(trackingWorkService.getByUser(id));
  }

  // FOLLOW — requiere autenticación (solo usuarios registrados pueden seguir)
  @Operation(summary = "Seguir usuario")
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{id}/follow/{targetId}")
  public ResponseEntity<String> followUser(@PathVariable Long id, @PathVariable Long targetId) {
    userService.followUser(id, targetId);
    return ResponseEntity.ok("Usuario seguido correctamente");
  }

  @Operation(summary = "Dejar de seguir usuario")
  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/{id}/follow/{targetId}")
  public ResponseEntity<String> unfollowUser(@PathVariable Long id, @PathVariable Long targetId) {
    userService.unfollowUser(id, targetId);
    return ResponseEntity.ok("Dejó de seguir correctamente");
  }

  @Operation(summary = "Obtener seguidores")
  @GetMapping("/{id}/followers")
  public ResponseEntity<List<UserResponseDTO>> getFollowers(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getFollowers(id));
  }

  @Operation(summary = "Obtener seguidos")
  @GetMapping("/{id}/following")
  public ResponseEntity<List<UserResponseDTO>> getFollowing(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getFollowing(id));
  }
}
