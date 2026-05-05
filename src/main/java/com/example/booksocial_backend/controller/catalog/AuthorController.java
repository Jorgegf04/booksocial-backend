package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * Controlador REST encargado de la gestión de la entidad Author dentro del
 * módulo de catálogo.
 *
 * <p>
 * Este controlador expone los endpoints necesarios para realizar operaciones
 * CRUD
 * y consultas avanzadas sobre autores en el sistema BookSocial.
 * </p>
 *
 * <p>
 * Forma parte del subsistema de gestión de catálogo definido en la arquitectura
 * del sistema,
 * permitiendo la interacción con los recursos Author mediante API REST
 * siguiendo
 * el estándar HTTP.
 * </p>
 *
 * <p>
 * <b>Responsabilidades principales:</b>
 * </p>
 * <ul>
 * <li>Registro y gestión de autores</li>
 * <li>Consulta individual y listados</li>
 * <li>Búsqueda y filtrado</li>
 * <li>Ranking y estadísticas básicas</li>
 * </ul>
 *
 * <p>
 * Todas las respuestas se devuelven en formato JSON y están diseñadas para ser
 * consumidas
 * por clientes REST (frontend, Postman, Swagger UI, etc.).
 * </p>
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

  // =========================
  // CREATE
  // =========================

  /**
   * Crea un nuevo autor en el sistema.
   *
   * @param request DTO con los datos del autor a registrar
   * @return Autor creado con su información completa
   */
  @Operation(summary = "Crear autor", description = "Registra un nuevo autor en el sistema. El autor podrá asociarse posteriormente a múltiples obras.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Autor creado correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos en la petición"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping
  public ResponseEntity<AuthorResponseDTO> createAuthor(
      @Valid @RequestBody AuthorRequestDTO request) {

    AuthorResponseDTO author = authorService.createAuthor(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(author);
  }

  /**
   * Crea múltiples autores en una única operación.
   *
   * @param requests lista de autores a registrar
   * @return lista de autores creados
   */
  @Operation(summary = "Creación masiva de autores", description = "Permite registrar múltiples autores en una única petición. Ideal para cargas iniciales de datos.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Autores creados correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista vacía o datos inválidos"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping("/batch")
  public ResponseEntity<List<AuthorResponseDTO>> createMany(
      @Valid @RequestBody List<AuthorRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista de autores no puede estar vacía");
    }

    List<AuthorResponseDTO> authors = requests.stream()
        .map(authorService::createAuthor)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(authors);
  }

  // =========================
  // READ
  // =========================

  /**
   * Obtiene un autor por su identificador único.
   *
   * @param id ID del autor
   * @return Autor encontrado
   */
  @Operation(summary = "Obtener autor por ID", description = "Recupera un autor específico a partir de su identificador único.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Autor encontrado"),
      @ApiResponse(responseCode = "404", description = "Autor no encontrado")
  })
  @GetMapping("/{id}")
  public ResponseEntity<AuthorResponseDTO> getAuthorById(
      @Parameter(description = "ID único del autor", example = "1") @PathVariable Long id) {

    return ResponseEntity.ok(authorService.getAuthorById(id));
  }

  /**
   * Obtiene todos los autores del sistema.
   *
   * @return listado completo de autores
   */
  @Operation(summary = "Listar todos los autores", description = "Devuelve todos los autores registrados en el sistema sin filtros.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
  })
  @GetMapping
  public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {

    return ResponseEntity.ok(authorService.getAllAuthors());
  }

  /**
   * Busca autores por nombre.
   *
   * @param name texto a buscar
   * @return lista de autores coincidentes
   */
  @Operation(summary = "Buscar autores por nombre", description = "Realiza una búsqueda parcial (LIKE) sobre el nombre del autor.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Resultados encontrados"),
      @ApiResponse(responseCode = "400", description = "Parámetro inválido")
  })
  @GetMapping("/search")
  public ResponseEntity<List<AuthorResponseDTO>> searchByName(
      @Parameter(description = "Nombre o parte del nombre del autor", example = "Tolkien") @RequestParam String name) {

    return ResponseEntity.ok(authorService.searchAuthorsByName(name));
  }

  @Operation(summary = "Autores ordenados", description = "Devuelve los autores ordenados alfabéticamente por nombre.")
  @GetMapping("/ordered")
  public ResponseEntity<List<AuthorResponseDTO>> getOrderedAuthors() {

    return ResponseEntity.ok(authorService.getAuthorsOrderedByName());
  }

  @Operation(summary = "Autores con obras", description = "Obtiene únicamente los autores que tienen al menos una obra asociada.")
  @GetMapping("/with-works")
  public ResponseEntity<List<AuthorResponseDTO>> getAuthorsWithWorks() {

    return ResponseEntity.ok(authorService.getAuthorsWithWorks());
  }

  @Operation(summary = "Top autores", description = "Ranking de autores basado en número de obras asociadas.")
  @GetMapping("/top")
  public ResponseEntity<List<AuthorResponseDTO>> getTopAuthors() {

    return ResponseEntity.ok(authorService.getTopAuthors());
  }

  // =========================
  // UPDATE
  // =========================

  /**
   * Actualiza un autor existente.
   *
   * @param id      ID del autor
   * @param request nuevos datos
   * @return autor actualizado
   */
  @Operation(summary = "Actualizar autor", description = "Modifica los datos de un autor existente.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Autor actualizado"),
      @ApiResponse(responseCode = "404", description = "Autor no encontrado"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos")
  })
  @PutMapping("/{id}")
  public ResponseEntity<AuthorResponseDTO> updateAuthor(
      @Parameter(description = "ID del autor a actualizar", example = "1") @PathVariable Long id,
      @Valid @RequestBody AuthorRequestDTO request) {

    return ResponseEntity.ok(authorService.updateAuthor(id, request));
  }

  // =========================
  // DELETE
  // =========================

  /**
   * Elimina un autor del sistema.
   *
   * @param id ID del autor
   * @return respuesta sin contenido
   */
  @Operation(summary = "Eliminar autor", description = "Elimina permanentemente un autor del sistema.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Autor eliminado"),
      @ApiResponse(responseCode = "404", description = "Autor no encontrado")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAuthor(
      @Parameter(description = "ID del autor a eliminar", example = "1") @PathVariable Long id) {

    authorService.deleteAuthor(id);
    return ResponseEntity.noContent().build();
  }

  // =========================
  // FOLLOW
  // =========================

  @Operation(summary = "Seguir autor")
  @PostMapping("/{id}/follow")
  public ResponseEntity<Void> followAuthor(
      @PathVariable Long id,
      @RequestParam Long userId) {
    authorService.followAuthor(userId, id);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Dejar de seguir autor")
  @DeleteMapping("/{id}/follow")
  public ResponseEntity<Void> unfollowAuthor(
      @PathVariable Long id,
      @RequestParam Long userId) {
    authorService.unfollowAuthor(userId, id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "¿El usuario sigue a este autor?")
  @GetMapping("/{id}/following")
  public ResponseEntity<Boolean> isFollowing(
      @PathVariable Long id,
      @RequestParam Long userId) {
    return ResponseEntity.ok(authorService.isFollowing(userId, id));
  }

  @Operation(summary = "Seguidores del autor")
  @GetMapping("/{id}/followers")
  public ResponseEntity<List<UserResponseDTO>> getFollowers(@PathVariable Long id) {
    return ResponseEntity.ok(authorService.getFollowers(id));
  }
}