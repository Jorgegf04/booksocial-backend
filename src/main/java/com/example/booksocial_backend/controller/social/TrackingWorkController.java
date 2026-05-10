package com.example.booksocial_backend.controller.social;

import java.util.List;

import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.social.*;
import com.example.booksocial_backend.service.TrackingWorkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión del seguimiento de obras.
 *
 * @author Jorge
 * @since 2026
 */
@RestController
@RequestMapping("/api/tracking-works")
@RequiredArgsConstructor
@Tag(name = "TrackingWork Controller", description = "API REST para gestión del seguimiento de obras")
public class TrackingWorkController {

  private final TrackingWorkService service;

  // CREATE — requiere autenticación (solo usuarios registrados pueden seguir obras)
  @Operation(summary = "Seguir obra")
  @PreAuthorize("isAuthenticated()")
  @PostMapping
  public ResponseEntity<TrackingWorkResponseDTO> create(@RequestBody TrackingWorkRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
  }

  @Operation(summary = "Seguimiento masivo")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/batch")
  public ResponseEntity<List<TrackingWorkResponseDTO>> createMany(@RequestBody List<TrackingWorkRequestDTO> requests) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.createMany(requests));
  }

  // READ — público (estadísticas visibles por todos)
  @Operation(summary = "Obras seguidas por usuario")
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<TrackingWorkResponseDTO>> getByUser(@PathVariable Long userId) {
    return ResponseEntity.ok(service.getByUser(userId));
  }

  @Operation(summary = "Usuarios que siguen una obra")
  @GetMapping("/work/{workId}")
  public ResponseEntity<List<TrackingWorkResponseDTO>> getByWork(@PathVariable Long workId) {
    return ResponseEntity.ok(service.getByWork(workId));
  }

  // UPDATE / DELETE — requiere autenticación
  @Operation(summary = "Actualizar estado de seguimiento")
  @PreAuthorize("isAuthenticated()")
  @PutMapping("/{id}")
  public ResponseEntity<TrackingWorkResponseDTO> updateStatus(@PathVariable Long id,
      @RequestBody TrackingWorkRequestDTO request) {
    return ResponseEntity.ok(service.updateStatus(id, request));
  }

  @Operation(summary = "Eliminar seguimiento")
  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
