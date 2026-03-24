package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.social.CommentDTO;
import com.example.booksocial_backend.DTO.social.CreateCommentRequest;

/**
 * Servicio encargado de la gestión del sistema de comentarios de BookSocial.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
public interface CommentService {

  CommentDTO createComment(CreateCommentRequest request);

  CommentDTO replyToComment(Long parentId, CreateCommentRequest request);

  CommentDTO getCommentById(Long id);

  List<CommentDTO> getAllComments();

  List<CommentDTO> getCommentsByWork(Long workId);

  List<CommentDTO> getRootCommentsByWork(Long workId);

  List<CommentDTO> getReplies(Long commentId);

  List<CommentDTO> getCommentsByWorkOrdered(Long workId);

  void deleteComment(Long id);
}