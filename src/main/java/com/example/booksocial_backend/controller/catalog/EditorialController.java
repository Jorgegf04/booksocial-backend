package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.EditorialRequestDTO;
import com.example.booksocial_backend.DTO.catalog.EditorialResponseDTO;
import com.example.booksocial_backend.service.EditorialService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/editorials")
@RequiredArgsConstructor
@Tag(name = "Editorial Controller", description = "Gestión de editoriales")
public class EditorialController {

  private final EditorialService editorialService;

  // CREATE
  @PostMapping
  public ResponseEntity<EditorialResponseDTO> create(@RequestBody EditorialRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(editorialService.createEditorial(request));
  }

  // READ
  @GetMapping("/{id}")
  public ResponseEntity<EditorialResponseDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(editorialService.getEditorialById(id));
  }

  @GetMapping
  public ResponseEntity<List<EditorialResponseDTO>> getAll() {
    return ResponseEntity.ok(editorialService.getAllEditorials());
  }

  @GetMapping("/ordered")
  public ResponseEntity<List<EditorialResponseDTO>> getOrdered() {
    return ResponseEntity.ok(editorialService.getEditorialsOrdered());
  }

  @GetMapping("/search")
  public ResponseEntity<List<EditorialResponseDTO>> search(@RequestParam String name) {
    return ResponseEntity.ok(editorialService.searchEditorialsByName(name));
  }

  @GetMapping("/country/{country}")
  public ResponseEntity<List<EditorialResponseDTO>> getByCountry(@PathVariable String country) {
    return ResponseEntity.ok(editorialService.getEditorialsByCountry(country));
  }

  // UPDATE
  @PutMapping("/{id}")
  public ResponseEntity<EditorialResponseDTO> update(
      @PathVariable Long id,
      @RequestBody EditorialRequestDTO request) {

    return ResponseEntity.ok(editorialService.updateEditorial(id, request));
  }

  // DELETE
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    editorialService.deleteEditorial(id);
    return ResponseEntity.noContent().build();
  }
}