package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.social.Comment;
import com.example.booksocial_backend.DTO.social.CommentResponseDTO;
import com.example.booksocial_backend.DTO.social.CommentRequestDTO;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.domain.user.User;

import com.example.booksocial_backend.repository.CommentRepository;
import com.example.booksocial_backend.service.CommentService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link CommentService}.
 *
 * Gestiono la lógica de negocio del sistema social, incluyendo
 * la publicación de comentarios, respuestas jerárquicas y
 * recuperación estructurada de la información.
 *
 * Controlo la coherencia entre comentarios, usuarios y obras,
 * evitando inconsistencias y asegurando una estructura jerárquica válida.
 *
 * Además, limito la profundidad de las respuestas en los DTOs
 * para evitar problemas de recursividad infinita en la serialización JSON.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  @Override
  public CommentResponseDTO createComment(CommentRequestDTO request) {

    Comment comment = new Comment();

    comment.setContent(request.getContent().trim());
    comment.setDate(LocalDateTime.now());

    comment.setUser(User.builder().id(request.getUserId()).build());
    comment.setWork(Work.builder().id(request.getWorkId()).build());

    // Comentario raíz
    comment.setParent(null);

    validateComment(comment);

    Comment saved = commentRepository.save(comment);

    return mapToDTO(saved);
  }

  @Override
  public CommentResponseDTO replyToComment(Long parentId, CommentRequestDTO request) {

    Comment parent = getCommentEntityById(parentId);

    Comment reply = new Comment();

    reply.setContent(request.getContent().trim());
    reply.setDate(LocalDateTime.now());

    reply.setUser(User.builder().id(request.getUserId()).build());
    reply.setWork(Work.builder().id(request.getWorkId()).build());

    // Validar coherencia: misma obra
    if (!parent.getWork().getId().equals(request.getWorkId())) {
      throw new IllegalArgumentException("La respuesta debe pertenecer a la misma obra");
    }

    reply.setParent(parent);

    validateComment(reply);

    Comment saved = commentRepository.save(reply);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public CommentResponseDTO getCommentById(Long id) {

    return mapToDTO(getCommentEntityById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentResponseDTO> getAllComments() {

    return commentRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentResponseDTO> getCommentsByWork(Long workId) {

    return commentRepository.findByWorkId(workId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentResponseDTO> getRootCommentsByWork(Long workId) {

    return commentRepository.findByWorkIdAndParentIsNull(workId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentResponseDTO> getReplies(Long commentId) {

    return commentRepository.findByParentId(commentId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentResponseDTO> getCommentsByWorkOrdered(Long workId) {

    return commentRepository.findByWorkIdOrderByDateDesc(workId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public void deleteComment(Long id) {

    Comment comment = getCommentEntityById(id);

    commentRepository.delete(comment);
  }

  /**
   * Convierte una entidad Comment a CommentDTO.
   *
   * Incluye únicamente un nivel de respuestas para evitar recursividad infinita.
   */
  private CommentResponseDTO mapToDTO(Comment comment) {

    return new CommentResponseDTO(
        comment.getId(),
        comment.getContent(),
        comment.getDate(),
        comment.getUser().getId(),
        comment.getWork().getId(),
        comment.getParent() != null ? comment.getParent().getId() : null,
        mapReplies(comment.getReplies()));
  }

  /**
   * Mapea las respuestas de un comentario (solo un nivel).
   */
  private List<CommentResponseDTO> mapReplies(List<Comment> replies) {

    if (replies == null || replies.isEmpty()) {
      return List.of();
    }

    return replies.stream()
        .map(reply -> new CommentResponseDTO(
            reply.getId(),
            reply.getContent(),
            reply.getDate(),
            reply.getUser().getId(),
            reply.getWork().getId(),
            reply.getParent() != null ? reply.getParent().getId() : null,
            List.of() // 🔥 evita recursividad infinita
        ))
        .toList();
  }

  private Comment getCommentEntityById(Long id) {

    return commentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Comentario no encontrado con id: " + id));
  }

  /**
   * Valida los datos básicos de un comentario.
   */
  private void validateComment(Comment comment) {

    if (comment == null) {
      throw new IllegalArgumentException("El comentario no puede ser nulo");
    }

    if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
      throw new IllegalArgumentException("El contenido del comentario es obligatorio");
    }

    if (comment.getUser() == null || comment.getUser().getId() == null) {
      throw new IllegalArgumentException("El comentario debe estar asociado a un usuario válido");
    }

    if (comment.getWork() == null || comment.getWork().getId() == null) {
      throw new IllegalArgumentException("El comentario debe estar asociado a una obra válida");
    }
  }
}