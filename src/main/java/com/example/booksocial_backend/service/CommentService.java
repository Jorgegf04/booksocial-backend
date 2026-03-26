package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.social.CommentResponseDTO;
import com.example.booksocial_backend.DTO.social.CommentRequestDTO;

/**
 * Servicio encargado de la gestión del sistema de comentarios de BookSocial.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
public interface CommentService {

  CommentResponseDTO createComment(CommentRequestDTO request);

  CommentResponseDTO replyToComment(Long parentId, CommentRequestDTO request);

  CommentResponseDTO getCommentById(Long id);

  List<CommentResponseDTO> getAllComments();

  List<CommentResponseDTO> getCommentsByWork(Long workId);

  List<CommentResponseDTO> getRootCommentsByWork(Long workId);

  List<CommentResponseDTO> getReplies(Long commentId);

  List<CommentResponseDTO> getCommentsByWorkOrdered(Long workId);

  void deleteComment(Long id);
}