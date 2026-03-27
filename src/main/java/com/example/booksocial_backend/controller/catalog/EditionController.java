package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.EditionRequestDTO;
import com.example.booksocial_backend.DTO.catalog.EditionResponseDTO;
import com.example.booksocial_backend.service.EditionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de ediciones dentro del sistema BookSocial.
 *
 * <p>
 * La entidad Edition representa una versión específica de una obra publicada
 * por una editorial, incluyendo información clave como ISBN, fecha de edición
 * y relación con el producto (precio y stock).
 * </p>
 *
 * <p>
 * Este controlador forma parte del módulo de catálogo y permite gestionar
 * las ediciones de forma independiente, facilitando la existencia de múltiples
 * versiones de una misma obra.
 * </p>
 *
 * <p><b>Responsabilidades:</b></p>
 * <ul>
 *   <li>Crear ediciones individuales o en lote</li>
 *   <li>Consultar ediciones por ID, ISBN o editorial</li>
 *   <li>Actualizar información de ediciones</li>
 *   <li>Eliminar ediciones</li>
 * </ul>
 *
 * <p>
 * Todos los endpoints siguen principios REST y devuelven datos en formato JSON.
 * </p>
 *
 * @author Jorge
 * @version 1.1
 * @since 2026
 */
@RestController
@RequestMapping("/api/editions")
@RequiredArgsConstructor
@Tag(
    name = "Edition Controller",
    description = "API REST para la gestión de ediciones de obras en el catálogo"
)
public class EditionController {

  private final EditionService editionService;

  // =========================
  // CREATE
  // =========================

  /**
   * Crea una nueva edición en el sistema.
   *
   * @param request datos de la edición
   * @return edición creada
   */
  @Operation(
      summary = "Crear edición",
      description = "Registra una nueva edición asociada a una obra y editorial."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Edición creada correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping
  public ResponseEntity<EditionResponseDTO> create(
      @Valid @RequestBody EditionRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(editionService.createEdition(request));
  }

  /**
   * Crea múltiples ediciones en una sola petición.
   *
   * @param requests lista de ediciones
   * @return lista de ediciones creadas
   */
  @Operation(
      summary = "Creación masiva de ediciones",
      description = "Permite registrar múltiples ediciones en una única operación."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Ediciones creadas correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista vacía o inválida"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PostMapping("/batch")
  public ResponseEntity<List<EditionResponseDTO>> createMany(
      @Valid @RequestBody List<EditionRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista de ediciones no puede estar vacía");
    }

    List<EditionResponseDTO> result = requests.stream()
        .map(editionService::createEdition)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  // =========================
  // READ
  // =========================

  /**
   * Obtiene una edición por su ID.
   *
   * @param id identificador de la edición
   * @return edición encontrada
   */
  @Operation(
      summary = "Obtener edición por ID",
      description = "Recupera una edición específica mediante su identificador único."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Edición encontrada"),
      @ApiResponse(responseCode = "404", description = "Edición no encontrada")
  })
  @GetMapping("/{id}")
  public ResponseEntity<EditionResponseDTO> getById(
      @Parameter(description = "ID de la edición", example = "1")
      @PathVariable Long id) {

    return ResponseEntity.ok(editionService.getEditionById(id));
  }

  /**
   * Obtiene todas las ediciones.
   *
   * @return listado de ediciones
   */
  @Operation(
      summary = "Listar todas las ediciones",
      description = "Devuelve todas las ediciones registradas en el sistema."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
  })
  @GetMapping
  public ResponseEntity<List<EditionResponseDTO>> getAll() {

    return ResponseEntity.ok(editionService.getAllEditions());
  }

  /**
   * Obtiene una edición a partir de su ISBN.
   *
   * @param isbn código ISBN de la edición
   * @return edición encontrada
   */
  @Operation(
      summary = "Buscar edición por ISBN",
      description = "Recupera una edición específica mediante su código ISBN."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Edición encontrada"),
      @ApiResponse(responseCode = "404", description = "Edición no encontrada")
  })
  @GetMapping("/isbn/{isbn}")
  public ResponseEntity<EditionResponseDTO> getByIsbn(
      @Parameter(description = "ISBN de la edición", example = "9780000000011")
      @PathVariable String isbn) {

    return ResponseEntity.ok(editionService.getEditionByIsbn(isbn));
  }

  /**
   * Obtiene todas las ediciones de una editorial.
   *
   * @param editorialId ID de la editorial
   * @return lista de ediciones
   */
  @Operation(
      summary = "Obtener ediciones por editorial",
      description = "Devuelve todas las ediciones asociadas a una editorial específica."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Ediciones encontradas"),
      @ApiResponse(responseCode = "404", description = "Editorial no encontrada")
  })
  @GetMapping("/editorial/{editorialId}")
  public ResponseEntity<List<EditionResponseDTO>> getByEditorial(
      @Parameter(description = "ID de la editorial", example = "1")
      @PathVariable Long editorialId) {

    return ResponseEntity.ok(editionService.getEditionsByEditorial(editorialId));
  }

  // =========================
  // UPDATE
  // =========================

  /**
   * Actualiza una edición existente.
   *
   * @param id ID de la edición
   * @param request nuevos datos
   * @return edición actualizada
   */
  @Operation(
      summary = "Actualizar edición",
      description = "Modifica los datos de una edición existente."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Edición actualizada correctamente"),
      @ApiResponse(responseCode = "404", description = "Edición no encontrada"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos")
  })
  @PutMapping("/{id}")
  public ResponseEntity<EditionResponseDTO> update(
      @Parameter(description = "ID de la edición a actualizar", example = "1")
      @PathVariable Long id,
      @Valid @RequestBody EditionRequestDTO request) {

    return ResponseEntity.ok(editionService.updateEdition(id, request));
  }

  // =========================
  // DELETE
  // =========================

  /**
   * Elimina una edición del sistema.
   *
   * @param id ID de la edición
   * @return respuesta sin contenido
   */
  @Operation(
      summary = "Eliminar edición",
      description = "Elimina permanentemente una edición del sistema."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Edición eliminada correctamente"),
      @ApiResponse(responseCode = "404", description = "Edición no encontrada")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "ID de la edición a eliminar", example = "1")
      @PathVariable Long id) {

    editionService.deleteEdition(id);
    return ResponseEntity.noContent().build();
  }
}