package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.AuthorDTO;
import com.example.booksocial_backend.DTO.catalog.CreateAuthorRequest;
import com.example.booksocial_backend.service.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de autores dentro del sistema BookSocial.
 *
 * Este controlador forma parte del módulo de catálogo y permite realizar
 * operaciones CRUD sobre los autores, así como consultas avanzadas
 * para la exploración del catálogo.
 *
 * Proporciona endpoints preparados para ser consumidos por clientes REST
 * como Postman o aplicaciones frontend.
 *
 * Funcionalidades principales:
 * - Creación de autores
 * - Consulta de autores
 * - Búsqueda y filtrado
 * - Ranking de autores
 * - Actualización y eliminación
 *
 * @author Jorge
 * @since 2026
 * @version 1.0
 */

@Tag(name = "Author Controller", description = "Gestión completa de autores dentro del catálogo literario del sistema BookSocial")
@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

  private final AuthorService authorService;

  // =========================
  // CREATE
  // =========================

  @Operation(summary = "Crear nuevo autor", description = "Registra un nuevo autor en el sistema con los datos proporcionados. "
      + "El autor podrá posteriormente asociarse a una o varias obras.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Autor creado correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping
  public ResponseEntity<AuthorDTO> createAuthor(@RequestBody CreateAuthorRequest request) {

    AuthorDTO author = authorService.createAuthor(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(author);
  }

  @Operation(summary = "Crear múltiples autores", description = "Permite la creación masiva de autores en una única petición. "
      + "Optimiza operaciones de carga inicial o importación de datos.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Autores creados correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista vacía o datos inválidos"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping("/batch")
  public ResponseEntity<List<AuthorDTO>> createMany(
      @RequestBody List<CreateAuthorRequest> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista no puede estar vacía");
    }

    List<AuthorDTO> authors = requests.stream()
        .map(authorService::createAuthor)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(authors);
  }

  // =========================
  // READ
  // =========================

  @Operation(summary = "Obtener autor por ID", description = "Recupera la información completa de un autor a partir de su identificador único.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Autor encontrado"),
      @ApiResponse(responseCode = "404", description = "Autor no encontrado")
  })
  @GetMapping("/{id}")
  public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {

    return ResponseEntity.ok(authorService.getAuthorById(id));
  }

  @Operation(summary = "Obtener todos los autores", description = "Devuelve el listado completo de autores registrados en el sistema.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
  })
  @GetMapping
  public ResponseEntity<List<AuthorDTO>> getAllAuthors() {

    return ResponseEntity.ok(authorService.getAllAuthors());
  }

  @Operation(summary = "Buscar autores por nombre", description = "Realiza una búsqueda parcial de autores por nombre. "
      + "El sistema devolverá coincidencias que contengan el texto indicado.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Resultados de búsqueda"),
      @ApiResponse(responseCode = "400", description = "Parámetro de búsqueda inválido")
  })
  @GetMapping("/search")
  public ResponseEntity<List<AuthorDTO>> searchByName(@RequestParam String name) {

    return ResponseEntity.ok(authorService.searchAuthorsByName(name));
  }

  @Operation(summary = "Obtener autores ordenados alfabéticamente", description = "Devuelve los autores ordenados de forma ascendente según su nombre.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Autores ordenados correctamente")
  })
  @GetMapping("/ordered")
  public ResponseEntity<List<AuthorDTO>> getOrderedAuthors() {

    return ResponseEntity.ok(authorService.getAuthorsOrderedByName());
  }

  @Operation(summary = "Obtener autores con obras asociadas", description = "Filtra y devuelve únicamente aquellos autores que tienen al menos una obra registrada en el sistema.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Autores con obras obtenidos correctamente")
  })
  @GetMapping("/with-works")
  public ResponseEntity<List<AuthorDTO>> getAuthorsWithWorks() {

    return ResponseEntity.ok(authorService.getAuthorsWithWorks());
  }

  @Operation(summary = "Ranking de autores", description = "Obtiene un ranking de autores en función del número de obras asociadas, "
      + "permitiendo identificar los autores más relevantes del catálogo.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Ranking generado correctamente")
  })
  @GetMapping("/top")
  public ResponseEntity<List<AuthorDTO>> getTopAuthors() {

    return ResponseEntity.ok(authorService.getTopAuthors());
  }

  // =========================
  // UPDATE
  // =========================

  @Operation(summary = "Actualizar autor", description = "Permite modificar los datos de un autor existente mediante su identificador.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Autor actualizado correctamente"),
      @ApiResponse(responseCode = "404", description = "Autor no encontrado"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos")
  })
  @PutMapping("/{id}")
  public ResponseEntity<AuthorDTO> updateAuthor(
      @PathVariable Long id,
      @RequestBody CreateAuthorRequest request) {

    return ResponseEntity.ok(authorService.updateAuthor(id, request));
  }

  // =========================
  // DELETE
  // =========================

  @Operation(summary = "Eliminar autor", description = "Elimina un autor del sistema mediante su identificador. "
      + "Esta operación es irreversible.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Autor eliminado correctamente"),
      @ApiResponse(responseCode = "404", description = "Autor no encontrado")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {

    authorService.deleteAuthor(id);
    return ResponseEntity.noContent().build();
  }
}