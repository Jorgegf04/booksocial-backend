package com.example.booksocial_backend.controller.social;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.social.CommentRequestDTO;
import com.example.booksocial_backend.DTO.social.CommentResponseDTO;
import com.example.booksocial_backend.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST encargado de la gestión del sistema de comentarios
 * dentro del módulo social de BookSocial.
 *
 * <p>
 * Permite a los usuarios publicar comentarios sobre obras, responder
 * a otros comentarios y consultar estructuras jerárquicas.
 * </p>
 *
 * <p>
 * <b>Responsabilidades:</b>
 * </p>
 * <ul>
 * <li>Publicación de comentarios</li>
 * <li>Respuestas a comentarios</li>
 * <li>Consulta por obra y usuario</li>
 * <li>Obtención de estructura jerárquica</li>
 * </ul>
 *
 * @author Jorge
 * @since 2026
 */
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comment Controller", description = "API REST para la gestión del sistema de comentarios")
public class CommentController {

  private final CommentService commentService;

  // =========================
  // CREATE
  // =========================

  @Operation(summary = "Crear comentario", description = "Publica un comentario raíz sobre una obra.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Comentario creado correctamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos")
  })
  @PostMapping
  public ResponseEntity<CommentResponseDTO> createComment(
      @Valid @RequestBody CommentRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(commentService.createComment(request));
  }

  @Operation(summary = "Responder comentario", description = "Permite responder a un comentario existente.")
  @PostMapping("/{parentId}/reply")
  public ResponseEntity<CommentResponseDTO> reply(
      @Parameter(description = "ID del comentario padre", example = "1") @PathVariable Long parentId,
      @Valid @RequestBody CommentRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(commentService.replyToComment(parentId, request));
  }

  @Operation(summary = "Creación masiva de comentarios", description = "Permite crear múltiples comentarios rápidamente.")
  @PostMapping("/batch")
  public ResponseEntity<List<CommentResponseDTO>> createMany(
      @Valid @RequestBody List<CommentRequestDTO> requests) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(requests.stream()
            .map(commentService::createComment)
            .toList());
  }

  // =========================
  // READ
  // =========================

  @Operation(summary = "Obtener comentario por ID", description = "Recupera un comentario específico.")
  @GetMapping("/{id}")
  public ResponseEntity<CommentResponseDTO> getById(
      @Parameter(description = "ID del comentario", example = "1") @PathVariable Long id) {

    return ResponseEntity.ok(commentService.getCommentById(id));
  }

  @Operation(summary = "Listar todos los comentarios", description = "Devuelve todos los comentarios del sistema.")
  @GetMapping
  public ResponseEntity<List<CommentResponseDTO>> getAll() {

    return ResponseEntity.ok(commentService.getAllComments());
  }

  @Operation(summary = "Comentarios por obra", description = "Obtiene todos los comentarios de una obra.")
  @GetMapping("/work/{workId}")
  public ResponseEntity<List<CommentResponseDTO>> getByWork(
      @Parameter(description = "ID de la obra", example = "1") @PathVariable Long workId) {

    return ResponseEntity.ok(commentService.getCommentsByWork(workId));
  }

  @Operation(summary = "Comentarios raíz por obra", description = "Obtiene comentarios principales (sin padre).")
  @GetMapping("/work/{workId}/root")
  public ResponseEntity<List<CommentResponseDTO>> getRootComments(
      @PathVariable Long workId) {

    return ResponseEntity.ok(commentService.getRootCommentsByWork(workId));
  }

  @Operation(summary = "Respuestas de un comentario", description = "Obtiene las respuestas de un comentario.")
  @GetMapping("/{id}/replies")
  public ResponseEntity<List<CommentResponseDTO>> getReplies(
      @PathVariable Long id) {

    return ResponseEntity.ok(commentService.getReplies(id));
  }

  @Operation(summary = "Comentarios ordenados", description = "Obtiene comentarios de una obra ordenados por fecha.")
  @GetMapping("/work/{workId}/ordered")
  public ResponseEntity<List<CommentResponseDTO>> getOrdered(
      @PathVariable Long workId) {

    return ResponseEntity.ok(commentService.getCommentsByWorkOrdered(workId));
  }

  // =========================
  // DELETE
  // =========================

  @Operation(summary = "Eliminar comentario", description = "Elimina un comentario del sistema.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "ID del comentario", example = "1") @PathVariable Long id) {

    commentService.deleteComment(id);
    return ResponseEntity.noContent().build();
  }
}