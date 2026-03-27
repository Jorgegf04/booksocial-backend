package com.example.booksocial_backend.controller.social;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.social.ReactionRequestDTO;
import com.example.booksocial_backend.DTO.social.ReactionResponseDTO;
import com.example.booksocial_backend.service.ReactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST encargado del sistema de reacciones (likes).
 *
 * <p>
 * Permite a los usuarios interactuar con comentarios mediante likes,
 * incluyendo funcionalidad toggle.
 * </p>
 */
@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
@Tag(name = "Reaction Controller", description = "API REST para la gestión de likes en comentarios")
public class ReactionController {

  private final ReactionService reactionService;

  // CREATE / TOGGLE
  @Operation(summary = "Toggle reacción", description = "Añade o elimina un like sobre un comentario.")
  @PostMapping
  public ResponseEntity<ReactionResponseDTO> toggle(
      @Valid @RequestBody ReactionRequestDTO request) {

    return ResponseEntity.ok(reactionService.toggleReaction(request));
  }

  // READ
  @Operation(summary = "Reacciones por comentario")
  @GetMapping("/comment/{commentId}")
  public ResponseEntity<List<ReactionResponseDTO>> getByComment(
      @Parameter(description = "ID del comentario") @PathVariable Long commentId) {

    return ResponseEntity.ok(reactionService.getReactionsByComment(commentId));
  }

  @Operation(summary = "Contar reacciones")
  @GetMapping("/comment/{commentId}/count")
  public ResponseEntity<Integer> count(
      @PathVariable Long commentId) {

    return ResponseEntity.ok(reactionService.countReactionsByComment(commentId));
  }

  // DELETE
  @Operation(summary = "Eliminar reacción")
  @DeleteMapping
  public ResponseEntity<Void> delete(
      @RequestParam Long userId,
      @RequestParam Long commentId) {

    reactionService.removeReaction(userId, commentId);
    return ResponseEntity.noContent().build();
  }
}