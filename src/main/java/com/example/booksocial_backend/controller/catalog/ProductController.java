package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.ProductRequestDTO;
import com.example.booksocial_backend.DTO.catalog.ProductResponseDTO;
import com.example.booksocial_backend.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "Gestión de productos (stock y venta)")
public class ProductController {

  private final ProductService productService;

  // CREATE
  @PostMapping
  public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(productService.createProduct(request));
  }

  // READ
  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }

  @GetMapping
  public ResponseEntity<List<ProductResponseDTO>> getAll() {
    return ResponseEntity.ok(productService.getAllProducts());
  }

  @GetMapping("/available")
  public ResponseEntity<List<ProductResponseDTO>> getAvailable() {
    return ResponseEntity.ok(productService.getAvailableProducts());
  }

  @GetMapping("/edition/{editionId}")
  public ResponseEntity<List<ProductResponseDTO>> getByEdition(@PathVariable Long editionId) {
    return ResponseEntity.ok(productService.getProductsByEdition(editionId));
  }

  @GetMapping("/work/{workId}")
  public ResponseEntity<List<ProductResponseDTO>> getByWork(@PathVariable Long workId) {
    return ResponseEntity.ok(productService.getProductsByWork(workId));
  }

  // UPDATE
  @PutMapping("/{id}")
  public ResponseEntity<ProductResponseDTO> update(
      @PathVariable Long id,
      @RequestBody ProductRequestDTO request) {

    return ResponseEntity.ok(productService.updateProduct(id, request));
  }

  // EXTRA (muy importante para tu sistema de compra)
  @PatchMapping("/{id}/decrease-stock")
  public ResponseEntity<Void> decreaseStock(
      @PathVariable Long id,
      @RequestParam int quantity) {

    productService.decreaseStock(id, quantity);
    return ResponseEntity.noContent().build();
  }

  // DELETE
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}