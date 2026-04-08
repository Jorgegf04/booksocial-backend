package com.example.booksocial_backend.controller.social;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.social.EventRequestDTO;
import com.example.booksocial_backend.DTO.social.EventResponseDTO;
import com.example.booksocial_backend.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de eventos exclusivos.
 *
 * <p>
 * Permite crear, consultar, actualizar y eliminar eventos
 * disponibles para usuarios suscritos.
 * </p>
 */
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Event Controller", description = "API REST para la gestión de eventos exclusivos")
public class EventController {

  private final EventService eventService;

  // CREATE
  @Operation(summary = "Crear evento")
  @PostMapping
  public ResponseEntity<EventResponseDTO> create(
      @Valid @RequestBody EventRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(eventService.createEvent(request));
  }

  @Operation(summary = "Creación masiva de eventos")
  @PostMapping("/batch")
  public ResponseEntity<List<EventResponseDTO>> createMany(
      @Valid @RequestBody List<EventRequestDTO> requests) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(requests.stream()
            .map(eventService::createEvent)
            .toList());
  }

  // READ
  @Operation(summary = "Obtener evento por ID")
  @GetMapping("/{id}")
  public ResponseEntity<EventResponseDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(eventService.getEventById(id));
  }

  @Operation(summary = "Listar eventos")
  @GetMapping
  public ResponseEntity<List<EventResponseDTO>> getAll() {
    return ResponseEntity.ok(eventService.getAllEvents());
  }

  @Operation(summary = "Eventos ordenados")
  @GetMapping("/ordered")
  public ResponseEntity<List<EventResponseDTO>> getOrdered() {
    return ResponseEntity.ok(eventService.getAllEventsOrdered());
  }

  @Operation(summary = "Eventos futuros")
  @GetMapping("/upcoming")
  public ResponseEntity<List<EventResponseDTO>> getUpcoming() {
    return ResponseEntity.ok(eventService.getUpcomingEvents());
  }

  // UPDATE
  @Operation(summary = "Actualizar evento")
  @PutMapping("/{id}")
  public ResponseEntity<EventResponseDTO> update(
      @PathVariable Long id,
      @Valid @RequestBody EventRequestDTO request) {

    return ResponseEntity.ok(eventService.updateEvent(id, request));
  }

  // JOIN / LEAVE
  @Operation(summary = "Inscribirse en evento")
  @PostMapping("/{id}/join")
  public ResponseEntity<EventResponseDTO> join(
      @PathVariable Long id,
      @RequestParam Long userId) {
    return ResponseEntity.ok(eventService.joinEvent(id, userId));
  }

  @Operation(summary = "Abandonar evento")
  @DeleteMapping("/{id}/leave")
  public ResponseEntity<EventResponseDTO> leave(
      @PathVariable Long id,
      @RequestParam Long userId) {
    return ResponseEntity.ok(eventService.leaveEvent(id, userId));
  }

  // DELETE
  @Operation(summary = "Eliminar evento")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    eventService.deleteEvent(id);
    return ResponseEntity.noContent().build();
  }
}