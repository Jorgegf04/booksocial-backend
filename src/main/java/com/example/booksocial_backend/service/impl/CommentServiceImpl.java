package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.social.Comment;
import com.example.booksocial_backend.DTO.social.CommentDTO;
import com.example.booksocial_backend.DTO.social.CreateCommentRequest;
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
  public CommentDTO createComment(CreateCommentRequest request) {

    Comment comment = new Comment();

    comment.setContent(request.content().trim());
    comment.setDate(LocalDateTime.now());

    comment.setUser(User.builder().id(request.userId()).build());
    comment.setWork(Work.builder().id(request.workId()).build());

    // Comentario raíz
    comment.setParent(null);

    validateComment(comment);

    Comment saved = commentRepository.save(comment);

    return mapToDTO(saved);
  }

  @Override
  public CommentDTO replyToComment(Long parentId, CreateCommentRequest request) {

    Comment parent = getCommentEntityById(parentId);

    Comment reply = new Comment();

    reply.setContent(request.content().trim());
    reply.setDate(LocalDateTime.now());

    reply.setUser(User.builder().id(request.userId()).build());
    reply.setWork(Work.builder().id(request.workId()).build());

    // Validar coherencia: misma obra
    if (!parent.getWork().getId().equals(request.workId())) {
      throw new IllegalArgumentException("La respuesta debe pertenecer a la misma obra");
    }

    reply.setParent(parent);

    validateComment(reply);

    Comment saved = commentRepository.save(reply);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public CommentDTO getCommentById(Long id) {

    return mapToDTO(getCommentEntityById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentDTO> getAllComments() {

    return commentRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentDTO> getCommentsByWork(Long workId) {

    return commentRepository.findByWorkId(workId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentDTO> getRootCommentsByWork(Long workId) {

    return commentRepository.findByWorkIdAndParentIsNull(workId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentDTO> getReplies(Long commentId) {

    return commentRepository.findByParentId(commentId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentDTO> getCommentsByWorkOrdered(Long workId) {

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
  private CommentDTO mapToDTO(Comment comment) {

    return new CommentDTO(
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
  private List<CommentDTO> mapReplies(List<Comment> replies) {

    if (replies == null || replies.isEmpty()) {
      return List.of();
    }

    return replies.stream()
        .map(reply -> new CommentDTO(
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