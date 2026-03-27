package com.example.booksocial_backend.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.catalog.VolumeRequestDTO;
import com.example.booksocial_backend.DTO.catalog.VolumeResponseDTO;
import com.example.booksocial_backend.service.VolumeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/volumes")
@RequiredArgsConstructor
@Tag(name = "Volume Controller", description = "Gestión de volúmenes (cómics/mangas)")
public class VolumeController {

  private final VolumeService volumeService;

  // CREATE
  @PostMapping
  public ResponseEntity<VolumeResponseDTO> create(@RequestBody VolumeRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(volumeService.createVolume(request));
  }

  // READ
  @GetMapping("/{id}")
  public ResponseEntity<VolumeResponseDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(volumeService.getVolumeById(id));
  }

  @GetMapping
  public ResponseEntity<List<VolumeResponseDTO>> getAll() {
    return ResponseEntity.ok(volumeService.getAllVolumes());
  }

  @GetMapping("/edition/{editionId}")
  public ResponseEntity<List<VolumeResponseDTO>> getByEdition(@PathVariable Long editionId) {
    return ResponseEntity.ok(volumeService.getVolumesByEdition(editionId));
  }

  @GetMapping("/edition/{editionId}/ordered")
  public ResponseEntity<List<VolumeResponseDTO>> getOrdered(@PathVariable Long editionId) {
    return ResponseEntity.ok(volumeService.getVolumesByEditionOrdered(editionId));
  }

  // UPDATE
  @PutMapping("/{id}")
  public ResponseEntity<VolumeResponseDTO> update(
      @PathVariable Long id,
      @RequestBody VolumeRequestDTO request) {

    return ResponseEntity.ok(volumeService.updateVolume(id, request));
  }

  // DELETE
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    volumeService.deleteVolume(id);
    return ResponseEntity.noContent().build();
  }
}