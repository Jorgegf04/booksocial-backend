package com.example.booksocial_backend.controller.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.user.*;
import com.example.booksocial_backend.domain.user.Role;
import com.example.booksocial_backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de usuarios.
 *
 * Expone endpoints CRUD y operaciones adicionales sobre usuarios.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Gestión de usuarios")
public class UserController {

  private final UserService userService;

  // =========================
  // CREATE
  // =========================

  @Operation(summary = "Crear usuario")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Usuario creado"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos")
  })
  @PostMapping
  public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequestDTO request) {

    UserResponseDTO user = userService.createUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  // =========================
  // READ
  // =========================

  @Operation(summary = "Obtener usuario por ID")
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {

    return ResponseEntity.ok(userService.getUserById(id));
  }

  @Operation(summary = "Obtener todos los usuarios")
  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

    return ResponseEntity.ok(userService.getAllUsers());
  }

  @Operation(summary = "Obtener usuarios activos")
  @GetMapping("/active")
  public ResponseEntity<List<UserResponseDTO>> getActiveUsers() {

    return ResponseEntity.ok(userService.getActiveUsers());
  }

  @Operation(summary = "Buscar por rol")
  @GetMapping("/role/{role}")
  public ResponseEntity<List<UserResponseDTO>> getByRole(@PathVariable Role role) {

    return ResponseEntity.ok(userService.getUsersByRole(role));
  }

  // =========================
  // UPDATE
  // =========================

  @Operation(summary = "Actualizar usuario")
  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> updateUser(
      @PathVariable Long id,
      @RequestBody UpdateUserRequestDTO request) {

    return ResponseEntity.ok(userService.updateUser(id, request));
  }

  // =========================
  // DELETE
  // =========================

  @Operation(summary = "Eliminar usuario")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  // =========================
  // STATUS
  // =========================

  @Operation(summary = "Activar o desactivar usuario")
  @PatchMapping("/{id}/active")
  public ResponseEntity<UserResponseDTO> setActive(
      @PathVariable Long id,
      @RequestParam Boolean active) {

    return ResponseEntity.ok(userService.setUserActive(id, active));
  }
}