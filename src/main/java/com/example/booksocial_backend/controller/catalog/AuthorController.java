package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.AuthorRequestDTO;
import com.example.booksocial_backend.DTO.catalog.AuthorResponseDTO;
import com.example.booksocial_backend.DTO.user.UserResponseDTO;
import com.example.booksocial_backend.service.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de autores dentro del catálogo.
 *
 * @author Jorge
 * @version 1.1
 * @since 2026
 */
@Tag(name = "Author Controller", description = "API REST para la gestión completa de autores dentro del catálogo literario")
@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

  private final AuthorService authorService;

  // CREATE — solo ADMIN
  @Operation(summary = "Crear autor")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<AuthorResponseDTO> createAuthor(@Valid @RequestBody AuthorRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(request));
  }

  @Operation(summary = "Creación masiva de autores")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/batch")
  public ResponseEntity<List<AuthorResponseDTO>> createMany(@Valid @RequestBody List<AuthorRequestDTO> requests) {
    if (requests == null || requests.isEmpty())
      throw new IllegalArgumentException("La lista de autores no puede estar vacía");
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(requests.stream().map(authorService::createAuthor).toList());
  }

  // READ — público
  @Operation(summary = "Obtener autor por ID")
  @GetMapping("/{id}")
  public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable Long id) {
    return ResponseEntity.ok(authorService.getAuthorById(id));
  }

  @Operation(summary = "Listar todos los autores")
  @GetMapping
  public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {
    return ResponseEntity.ok(authorService.getAllAuthors());
  }

  @Operation(summary = "Buscar autores por nombre")
  @GetMapping("/search")
  public ResponseEntity<List<AuthorResponseDTO>> searchByName(@RequestParam String name) {
    return ResponseEntity.ok(authorService.searchAuthorsByName(name));
  }

  @Operation(summary = "Autores ordenados")
  @GetMapping("/ordered")
  public ResponseEntity<List<AuthorResponseDTO>> getOrderedAuthors() {
    return ResponseEntity.ok(authorService.getAuthorsOrderedByName());
  }

  @Operation(summary = "Autores con obras")
  @GetMapping("/with-works")
  public ResponseEntity<List<AuthorResponseDTO>> getAuthorsWithWorks() {
    return ResponseEntity.ok(authorService.getAuthorsWithWorks());
  }

  @Operation(summary = "Top autores")
  @GetMapping("/top")
  public ResponseEntity<List<AuthorResponseDTO>> getTopAuthors() {
    return ResponseEntity.ok(authorService.getTopAuthors());
  }

  // UPDATE / DELETE — solo ADMIN
  @Operation(summary = "Actualizar autor")
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<AuthorResponseDTO> updateAuthor(@PathVariable Long id,
      @Valid @RequestBody AuthorRequestDTO request) {
    return ResponseEntity.ok(authorService.updateAuthor(id, request));
  }

  @Operation(summary = "Eliminar autor")
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
    authorService.deleteAuthor(id);
    return ResponseEntity.noContent().build();
  }

  // FOLLOW — requiere autenticación (usuarios registrados pueden seguir autores)
  @Operation(summary = "Seguir autor")
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{id}/follow")
  public ResponseEntity<Void> followAuthor(@PathVariable Long id, @RequestParam Long userId) {
    authorService.followAuthor(userId, id);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Dejar de seguir autor")
  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/{id}/follow")
  public ResponseEntity<Void> unfollowAuthor(@PathVariable Long id, @RequestParam Long userId) {
    authorService.unfollowAuthor(userId, id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "¿El usuario sigue a este autor?")
  @GetMapping("/{id}/following")
  public ResponseEntity<Boolean> isFollowing(@PathVariable Long id, @RequestParam Long userId) {
    return ResponseEntity.ok(authorService.isFollowing(userId, id));
  }

  @Operation(summary = "Seguidores del autor")
  @GetMapping("/{id}/followers")
  public ResponseEntity<List<UserResponseDTO>> getFollowers(@PathVariable Long id) {
    return ResponseEntity.ok(authorService.getFollowers(id));
  }
}
