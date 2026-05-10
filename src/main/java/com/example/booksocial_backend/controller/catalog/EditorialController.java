package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.EditorialRequestDTO;
import com.example.booksocial_backend.DTO.catalog.EditorialResponseDTO;
import com.example.booksocial_backend.service.EditorialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de editoriales dentro del sistema
 * BookSocial.
 *
 * <p>
 * La entidad Editorial representa las empresas responsables de la publicación
 * de las distintas ediciones de obras dentro del catálogo.
 * </p>
 *
 * <p>
 * Este controlador permite realizar operaciones CRUD y consultas específicas
 * sobre editoriales, facilitando la organización del catálogo por origen,
 * nombre y relaciones editoriales.
 * </p>
 *
 * <p>
 * <b>Responsabilidades:</b>
 * </p>
 * <ul>
 * <li>Crear editoriales individuales o en lote</li>
 * <li>Consultar editoriales por ID, nombre o país</li>
 * <li>Listar editoriales ordenadas</li>
 * <li>Actualizar información</li>
 * <li>Eliminar editoriales</li>
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
@RequestMapping("/api/editorials")
@RequiredArgsConstructor
@Tag(name = "Editorial Controller", description = "API REST para la gestión de editoriales dentro del catálogo")
public class EditorialController {

  private final EditorialService editorialService;

  // =========================
  // CREATE
  // =========================

  /**
   * Crea una nueva editorial.
   *
   * @param request datos de la editorial
   * @return editorial creada
   */
  @Operation(summary = "Crear editorial", description = "Registra una nueva editorial en el sistema.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Editorial creada correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<EditorialResponseDTO> create(
      @Valid @RequestBody EditorialRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(editorialService.createEditorial(request));
  }

  /**
   * Crea múltiples editoriales en una única petición.
   *
   * @param requests lista de editoriales
   * @return lista de editoriales creadas
   */
  @Operation(summary = "Creación masiva de editoriales", description = "Permite registrar múltiples editoriales en una sola operación.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Editoriales creadas correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista vacía o inválida"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/batch")
  public ResponseEntity<List<EditorialResponseDTO>> createMany(
      @Valid @RequestBody List<EditorialRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista de editoriales no puede estar vacía");
    }

    List<EditorialResponseDTO> result = requests.stream()
        .map(editorialService::createEditorial)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  // =========================
  // READ
  // =========================

  /**
   * Obtiene una editorial por su ID.
   *
   * @param id identificador de la editorial
   * @return editorial encontrada
   */
  @Operation(summary = "Obtener editorial por ID", description = "Recupera una editorial específica mediante su identificador único.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Editorial encontrada"),
      @ApiResponse(responseCode = "404", description = "Editorial no encontrada")
  })
  @GetMapping("/{id}")
  public ResponseEntity<EditorialResponseDTO> getById(
      @Parameter(description = "ID de la editorial", example = "1") @PathVariable Long id) {

    return ResponseEntity.ok(editorialService.getEditorialById(id));
  }

  /**
   * Obtiene todas las editoriales.
   *
   * @return listado de editoriales
   */
  @Operation(summary = "Listar todas las editoriales", description = "Devuelve todas las editoriales registradas en el sistema.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
  })
  @GetMapping
  public ResponseEntity<List<EditorialResponseDTO>> getAll() {

    return ResponseEntity.ok(editorialService.getAllEditorials());
  }

  /**
   * Obtiene las editoriales ordenadas alfabéticamente.
   *
   * @return lista ordenada
   */
  @Operation(summary = "Editoriales ordenadas", description = "Devuelve las editoriales ordenadas alfabéticamente por nombre.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Listado ordenado correctamente")
  })
  @GetMapping("/ordered")
  public ResponseEntity<List<EditorialResponseDTO>> getOrdered() {

    return ResponseEntity.ok(editorialService.getEditorialsOrdered());
  }

  /**
   * Busca editoriales por nombre.
   *
   * @param name texto de búsqueda
   * @return lista de coincidencias
   */
  @Operation(summary = "Buscar editoriales por nombre", description = "Realiza una búsqueda parcial por nombre de editorial.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Resultados encontrados"),
      @ApiResponse(responseCode = "400", description = "Parámetro inválido")
  })
  @GetMapping("/search")
  public ResponseEntity<List<EditorialResponseDTO>> search(
      @Parameter(description = "Nombre o fragmento del nombre", example = "Planeta") @RequestParam String name) {

    return ResponseEntity.ok(editorialService.searchEditorialsByName(name));
  }

  /**
   * Obtiene editoriales por país.
   *
   * @param country país de la editorial
   * @return lista de editoriales
   */
  @Operation(summary = "Buscar editoriales por país", description = "Devuelve las editoriales filtradas por país de origen.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Resultados encontrados")
  })
  @GetMapping("/country/{country}")
  public ResponseEntity<List<EditorialResponseDTO>> getByCountry(
      @Parameter(description = "País de la editorial", example = "España") @PathVariable String country) {

    return ResponseEntity.ok(editorialService.getEditorialsByCountry(country));
  }

  // =========================
  // UPDATE
  // =========================

  /**
   * Actualiza una editorial existente.
   *
   * @param id      ID de la editorial
   * @param request nuevos datos
   * @return editorial actualizada
   */
  @Operation(summary = "Actualizar editorial", description = "Modifica los datos de una editorial existente.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Editorial actualizada correctamente"),
      @ApiResponse(responseCode = "404", description = "Editorial no encontrada"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<EditorialResponseDTO> update(
      @Parameter(description = "ID de la editorial", example = "1") @PathVariable Long id,
      @Valid @RequestBody EditorialRequestDTO request) {

    return ResponseEntity.ok(editorialService.updateEditorial(id, request));
  }

  // =========================
  // DELETE
  // =========================

  /**
   * Elimina una editorial del sistema.
   *
   * @param id ID de la editorial
   * @return respuesta sin contenido
   */
  @Operation(summary = "Eliminar editorial", description = "Elimina permanentemente una editorial del sistema.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Editorial eliminada correctamente"),
      @ApiResponse(responseCode = "404", description = "Editorial no encontrada")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "ID de la editorial a eliminar", example = "1") @PathVariable Long id) {

    editorialService.deleteEditorial(id);
    return ResponseEntity.noContent().build();
  }
}