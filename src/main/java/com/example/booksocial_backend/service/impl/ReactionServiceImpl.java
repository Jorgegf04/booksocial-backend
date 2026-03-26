package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.social.Reaction;
import com.example.booksocial_backend.DTO.social.ReactionResponseDTO;
import com.example.booksocial_backend.DTO.social.ReactionRequestDTO;
import com.example.booksocial_backend.domain.social.Comment;
import com.example.booksocial_backend.domain.user.User;

import com.example.booksocial_backend.repository.ReactionRepository;
import com.example.booksocial_backend.service.ReactionService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link ReactionService}.
 *
 * Gestiono la lógica de negocio del sistema de reacciones,
 * permitiendo a los usuarios dar o quitar "like" sobre comentarios.
 *
 * Aplico lógica de tipo toggle para mejorar la experiencia de usuario.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReactionServiceImpl implements ReactionService {

  private final ReactionRepository reactionRepository;

  @Override
  public ReactionResponseDTO toggleReaction(ReactionRequestDTO request) {

    boolean exists = reactionRepository
        .existsByUserIdAndCommentId(request.getUserId(), request.getCommentId());

    if (exists) {

      Reaction existing = reactionRepository
          .findByUserIdAndCommentId(request.getUserId(), request.getCommentId())
          .orElseThrow();

      reactionRepository.delete(existing);

      return null;
    }

    // 🔥 SI NO EXISTE → crear (toggle ON)
    Reaction reaction = Reaction.builder()
        .date(LocalDateTime.now())
        .user(User.builder().id(request.getUserId()).build())
        .comment(Comment.builder().id(request.getCommentId()).build())
        .build();

    Reaction saved = reactionRepository.save(reaction);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReactionResponseDTO> getReactionsByComment(Long commentId) {

    return reactionRepository.findByCommentId(commentId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public int countReactionsByComment(Long commentId) {

    return reactionRepository.findByCommentId(commentId).size();
  }

  @Override
  public void removeReaction(Long userId, Long commentId) {

    Reaction reaction = reactionRepository
        .findByUserIdAndCommentId(userId, commentId)
        .orElseThrow(() -> new RuntimeException("No existe reacción"));

    reactionRepository.delete(reaction);
  }

  private ReactionResponseDTO mapToDTO(Reaction reaction) {

    return new ReactionResponseDTO(
        reaction.getId(),
        reaction.getDate(),
        reaction.getUser().getId(),
        reaction.getComment().getId());
  }
}