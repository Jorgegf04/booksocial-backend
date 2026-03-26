package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.social.ReactionResponseDTO;
import com.example.booksocial_backend.DTO.social.ReactionRequestDTO;

/**
 * Servicio encargado de la gestión de reacciones (likes) dentro del sistema.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
public interface ReactionService {

  ReactionResponseDTO toggleReaction(ReactionRequestDTO request);

  List<ReactionResponseDTO> getReactionsByComment(Long commentId);

  int countReactionsByComment(Long commentId);

  void removeReaction(Long userId, Long commentId);
}