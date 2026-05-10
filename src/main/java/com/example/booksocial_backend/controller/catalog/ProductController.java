package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.ProductRequestDTO;
import com.example.booksocial_backend.DTO.catalog.ProductResponseDTO;
import com.example.booksocial_backend.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de productos dentro del sistema BookSocial.
 *
 * <p>
 * La entidad Product representa un elemento comercializable asociado a una
 * edición,
 * incluyendo información de precio y stock disponible.
 * </p>
 *
 * <p>
 * Este controlador forma parte del sistema de compra y permite gestionar el
 * inventario
 * y disponibilidad de productos dentro de la plataforma.
 * </p>
 *
 * <p>
 * <b>Responsabilidades:</b>
 * </p>
 * <ul>
 * <li>Crear productos individuales o en lote</li>
 * <li>Consultar productos por distintos criterios (ID, edición, obra,
 * disponibilidad)</li>
 * <li>Actualizar datos de producto</li>
 * <li>Gestionar stock (disminución)</li>
 * <li>Eliminar productos</li>
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "API REST para la gestión de productos, stock y disponibilidad")
public class ProductController {

  private final ProductService productService;

  // =========================
  // CREATE
  // =========================

  /**
   * Crea un nuevo producto.
   *
   * @param request datos del producto
   * @return producto creado
   */
  @Operation(summary = "Crear producto", description = "Registra un nuevo producto asociado a una edición con su precio y stock inicial.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<ProductResponseDTO> create(
      @Valid @RequestBody ProductRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(productService.createProduct(request));
  }

  /**
   * Crea múltiples productos en una única petición.
   *
   * @param requests lista de productos
   * @return lista de productos creados
   */
  @Operation(summary = "Creación masiva de productos", description = "Permite registrar múltiples productos asociados a distintas ediciones.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Productos creados correctamente"),
      @ApiResponse(responseCode = "400", description = "Lista vacía o inválida"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/batch")
  public ResponseEntity<List<ProductResponseDTO>> createMany(
      @Valid @RequestBody List<ProductRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista de productos no puede estar vacía");
    }

    List<ProductResponseDTO> result = requests.stream()
        .map(productService::createProduct)
        .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  // =========================
  // READ
  // =========================

  /**
   * Obtiene un producto por su ID.
   */
  @Operation(summary = "Obtener producto por ID", description = "Recupera un producto específico mediante su identificador.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Producto encontrado"),
      @ApiResponse(responseCode = "404", description = "Producto no encontrado")
  })
  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDTO> getById(
      @Parameter(description = "ID del producto", example = "1") @PathVariable Long id) {

    return ResponseEntity.ok(productService.getProductById(id));
  }

  /**
   * Obtiene todos los productos.
   */
  @Operation(summary = "Listar productos", description = "Devuelve todos los productos registrados en el sistema.")
  @GetMapping
  public ResponseEntity<List<ProductResponseDTO>> getAll() {

    return ResponseEntity.ok(productService.getAllProducts());
  }

  /**
   * Obtiene productos con stock disponible.
   */
  @Operation(summary = "Productos disponibles", description = "Devuelve únicamente los productos con stock mayor a cero.")
  @GetMapping("/available")
  public ResponseEntity<List<ProductResponseDTO>> getAvailable() {

    return ResponseEntity.ok(productService.getAvailableProducts());
  }

  /**
   * Obtiene productos por edición.
   */
  @Operation(summary = "Productos por edición", description = "Devuelve todos los productos asociados a una edición concreta.")
  @GetMapping("/edition/{editionId}")
  public ResponseEntity<List<ProductResponseDTO>> getByEdition(
      @Parameter(description = "ID de la edición", example = "1") @PathVariable Long editionId) {

    return ResponseEntity.ok(productService.getProductsByEdition(editionId));
  }

  /**
   * Obtiene productos por obra.
   */
  @Operation(summary = "Productos por obra", description = "Devuelve todos los productos asociados a una obra.")
  @GetMapping("/work/{workId}")
  public ResponseEntity<List<ProductResponseDTO>> getByWork(
      @Parameter(description = "ID de la obra", example = "1") @PathVariable Long workId) {

    return ResponseEntity.ok(productService.getProductsByWork(workId));
  }

  // =========================
  // UPDATE
  // =========================

  /**
   * Actualiza un producto existente.
   */
  @Operation(summary = "Actualizar producto", description = "Modifica los datos de un producto existente.")
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<ProductResponseDTO> update(
      @Parameter(description = "ID del producto", example = "1") @PathVariable Long id,
      @Valid @RequestBody ProductRequestDTO request) {

    return ResponseEntity.ok(productService.updateProduct(id, request));
  }

  // =========================
  // STOCK MANAGEMENT
  // =========================

  /**
   * Reduce el stock de un producto.
   *
   * <p>
   * Este endpoint se utiliza en el proceso de compra para garantizar
   * la consistencia del inventario.
   * </p>
   *
   * @param id       ID del producto
   * @param quantity cantidad a reducir
   */
  @Operation(summary = "Reducir stock", description = "Disminuye el stock de un producto tras una operación de compra.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Stock actualizado correctamente"),
      @ApiResponse(responseCode = "400", description = "Cantidad inválida"),
      @ApiResponse(responseCode = "404", description = "Producto no encontrado")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}/decrease-stock")
  public ResponseEntity<Void> decreaseStock(
      @Parameter(description = "ID del producto", example = "1") @PathVariable Long id,
      @Parameter(description = "Cantidad a reducir", example = "2") @RequestParam int quantity) {

    productService.decreaseStock(id, quantity);
    return ResponseEntity.noContent().build();
  }

  // =========================
  // DELETE
  // =========================

  /**
   * Elimina un producto del sistema.
   */
  @Operation(summary = "Eliminar producto", description = "Elimina permanentemente un producto del sistema.")
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "ID del producto", example = "1") @PathVariable Long id) {

    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}