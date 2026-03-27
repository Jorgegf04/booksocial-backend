package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.EditionRequestDTO;
import com.example.booksocial_backend.DTO.catalog.EditionResponseDTO;
import com.example.booksocial_backend.service.EditionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/editions")
@RequiredArgsConstructor
@Tag(name = "Edition Controller", description = "Gestión de ediciones de obras")
public class EditionController {

  private final EditionService editionService;

  // CREATE
  @PostMapping
  public ResponseEntity<EditionResponseDTO> create(@RequestBody EditionRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(editionService.createEdition(request));
  }

  // READ
  @GetMapping("/{id}")
  public ResponseEntity<EditionResponseDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(editionService.getEditionById(id));
  }

  @GetMapping
  public ResponseEntity<List<EditionResponseDTO>> getAll() {
    return ResponseEntity.ok(editionService.getAllEditions());
  }

  @GetMapping("/isbn/{isbn}")
  public ResponseEntity<EditionResponseDTO> getByIsbn(@PathVariable String isbn) {
    return ResponseEntity.ok(editionService.getEditionByIsbn(isbn));
  }

  @GetMapping("/editorial/{editorialId}")
  public ResponseEntity<List<EditionResponseDTO>> getByEditorial(@PathVariable Long editorialId) {
    return ResponseEntity.ok(editionService.getEditionsByEditorial(editorialId));
  }

  // UPDATE
  @PutMapping("/{id}")
  public ResponseEntity<EditionResponseDTO> update(
      @PathVariable Long id,
      @RequestBody EditionRequestDTO request) {

    return ResponseEntity.ok(editionService.updateEdition(id, request));
  }

  // DELETE
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    editionService.deleteEdition(id);
    return ResponseEntity.noContent().build();
  }
}