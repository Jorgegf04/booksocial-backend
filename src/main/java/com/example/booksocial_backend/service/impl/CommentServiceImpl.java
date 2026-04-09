package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.social.Comment;
import com.example.booksocial_backend.DTO.social.CommentResponseDTO;
import com.example.booksocial_backend.DTO.social.CommentRequestDTO;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.domain.user.User;

import com.example.booksocial_backend.repository.CommentRepository;
import com.example.booksocial_backend.repository.UserRepository;
import com.example.booksocial_backend.repository.WorkRepository;
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
  private final UserRepository userRepository;
  private final WorkRepository workRepository;

  @Override
  public CommentResponseDTO createComment(CommentRequestDTO request) {

    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    Work work = workRepository.findById(request.getWorkId())
        .orElseThrow(() -> new RuntimeException("Obra no encontrada"));

    Comment comment = new Comment();

    comment.setContent(request.getContent().trim());
    comment.setDate(LocalDateTime.now());

    comment.setUser(user);
    comment.setWork(work);

    comment.setParent(null);

    validateComment(comment);

    return mapToDTO(commentRepository.save(comment));
  }

  @Override
  public CommentResponseDTO replyToComment(Long parentId, CommentRequestDTO request) {

    Comment parent = getCommentEntityById(parentId);

    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    Work work = workRepository.findById(request.getWorkId())
        .orElseThrow(() -> new RuntimeException("Obra no encontrada"));

    if (!parent.getWork().getId().equals(request.getWorkId())) {
      throw new IllegalArgumentException("La respuesta debe pertenecer a la misma obra");
    }

    Comment reply = new Comment();

    reply.setContent(request.getContent().trim());
    reply.setDate(LocalDateTime.now());

    reply.setUser(user);
    reply.setWork(work);
    reply.setParent(parent);

    validateComment(reply);

    return mapToDTO(commentRepository.save(reply));
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

        comment.getUpdatedAt(),
        comment.getEdited(),

        comment.getUser().getId(),
        comment.getUser().getUsername(),

        comment.getWork().getId(),
        comment.getWork().getTitle(),

        comment.getParent() != null ? comment.getParent().getId() : null,

        mapReplies(comment.getReplies()));
  }

  /**
   * Mapea las respuestas de un comentario de forma recursiva (árbol completo).
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

            reply.getUpdatedAt(),
            reply.getEdited(),

            reply.getUser().getId(),
            reply.getUser().getUsername(),

            reply.getWork().getId(),
            reply.getWork().getTitle(),

            reply.getParent() != null ? reply.getParent().getId() : null,

            mapReplies(reply.getReplies())
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

  @Override
  public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO request) {

    Comment comment = getCommentEntityById(commentId);

    if (!comment.getUser().getId().equals(request.getUserId())) {
      throw new RuntimeException("No tienes permiso para editar este comentario");
    }

    comment.setContent(request.getContent().trim());

    // Auditoría
    comment.setUpdatedAt(LocalDateTime.now());
    comment.setEdited(true);

    return mapToDTO(commentRepository.save(comment));
  }
}