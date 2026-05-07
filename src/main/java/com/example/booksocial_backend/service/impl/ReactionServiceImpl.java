package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.social.Reaction;
import com.example.booksocial_backend.DTO.social.ReactionResponseDTO;
import com.example.booksocial_backend.DTO.social.ReactionRequestDTO;
import com.example.booksocial_backend.domain.social.Comment;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.exception.ReactionNotFoundException;
import com.example.booksocial_backend.repository.ReactionRepository;
import com.example.booksocial_backend.service.ReactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReactionServiceImpl implements ReactionService {

  private static final Logger log = LoggerFactory.getLogger(ReactionServiceImpl.class);

  private final ReactionRepository reactionRepository;

  @Override
  public ReactionResponseDTO toggleReaction(ReactionRequestDTO request) {

    log.info("[REACTION] [TOGGLE] userId={} commentId={}", request.getUserId(), request.getCommentId());

    boolean exists = reactionRepository
        .existsByUserIdAndCommentId(request.getUserId(), request.getCommentId());

    if (exists) {
      Reaction existing = reactionRepository
          .findByUserIdAndCommentId(request.getUserId(), request.getCommentId())
          .orElseThrow(() -> new ReactionNotFoundException(request.getUserId(), request.getCommentId()));

      String username = existing.getUser().getUsername();
      reactionRepository.delete(existing);
      log.info("[REACTION] [TOGGLE] [REMOVED] userId={} commentId={}", request.getUserId(), request.getCommentId());

      return new ReactionResponseDTO(null, null, request.getUserId(), username, request.getCommentId(), false);
    }

    Reaction reaction = Reaction.builder()
        .date(LocalDateTime.now())
        .user(User.builder().id(request.getUserId()).build())
        .comment(Comment.builder().id(request.getCommentId()).build())
        .build();

    Reaction saved = reactionRepository.save(reaction);
    log.info("[REACTION] [TOGGLE] [ADDED] id={} userId={} commentId={}", saved.getId(), request.getUserId(), request.getCommentId());

    return new ReactionResponseDTO(
        saved.getId(),
        saved.getDate(),
        saved.getUser().getId(),
        saved.getUser().getUsername(),
        saved.getComment().getId(),
        true);
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
    log.info("[REACTION] [REMOVE] userId={} commentId={}", userId, commentId);
    Reaction reaction = reactionRepository
        .findByUserIdAndCommentId(userId, commentId)
        .orElseThrow(() -> new ReactionNotFoundException(userId, commentId));
    reactionRepository.delete(reaction);
    log.info("[REACTION] [REMOVE] [SUCCESS] userId={} commentId={}", userId, commentId);
  }

  private ReactionResponseDTO mapToDTO(Reaction reaction) {
    return new ReactionResponseDTO(
        reaction.getId(),
        reaction.getDate(),
        reaction.getUser().getId(),
        reaction.getUser().getUsername(),
        reaction.getComment().getId(),
        true);
  }
}
