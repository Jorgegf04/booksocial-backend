package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.social.ReactionDTO;
import com.example.booksocial_backend.DTO.social.ToggleReactionRequest;

/**
 * Servicio encargado de la gestión de reacciones (likes) dentro del sistema.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
public interface ReactionService {

  ReactionDTO toggleReaction(ToggleReactionRequest request);

  List<ReactionDTO> getReactionsByComment(Long commentId);

  int countReactionsByComment(Long commentId);

  void removeReaction(Long userId, Long commentId);
}