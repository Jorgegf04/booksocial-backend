package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.booksocial_backend.DTO.social.ReactionRequestDTO;
import com.example.booksocial_backend.domain.social.Reaction;
import com.example.booksocial_backend.domain.social.Comment;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.repository.ReactionRepository;
import com.example.booksocial_backend.service.impl.ReactionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
/**
 * Tests unitarios de {@link com.example.booksocial_backend.service.impl.ReactionServiceImpl}.
 *
 * <p>Cubre el mecanismo de toggle de likes: si el usuario aún no ha reaccionado se crea
 * una nueva {@link com.example.booksocial_backend.domain.social.Reaction} (liked=true);
 * si ya existe se elimina (liked=false). También verifica el conteo de reacciones por
 * comentario y la eliminación directa mediante {@code removeReaction}.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
class ReactionServiceImplTest {

  @Mock
  private ReactionRepository reactionRepository;

  @InjectMocks
  private ReactionServiceImpl service;

  private Reaction reaction;

  @BeforeEach
  void setUp() {

    User user = new User();
    user.setId(1L);
    user.setUsername("jorge");

    Comment comment = new Comment();
    comment.setId(1L);

    reaction = new Reaction();
    reaction.setId(1L);
    reaction.setDate(LocalDateTime.now());
    reaction.setUser(user);
    reaction.setComment(comment);
  }

  // =========================
  // TOGGLE ON
  // =========================

  @Test
  void shouldAddReaction() {

    ReactionRequestDTO request = new ReactionRequestDTO(1L, 1L);

    when(reactionRepository.existsByUserIdAndCommentId(1L, 1L)).thenReturn(false);
    when(reactionRepository.save(any())).thenReturn(reaction);

    var result = service.toggleReaction(request);

    assertTrue(result.getLiked());
    assertEquals(1L, result.getUserId());
    assertEquals(1L, result.getCommentId());
  }

  // =========================
  // TOGGLE OFF
  // =========================

  @Test
  void shouldRemoveReactionWhenAlreadyExists() {

    ReactionRequestDTO request = new ReactionRequestDTO(1L, 1L);

    when(reactionRepository.existsByUserIdAndCommentId(1L, 1L)).thenReturn(true);
    when(reactionRepository.findByUserIdAndCommentId(1L, 1L))
        .thenReturn(Optional.of(reaction));

    var result = service.toggleReaction(request);

    assertFalse(result.getLiked());
    verify(reactionRepository).delete(reaction);
  }

  // =========================
  // GET BY COMMENT
  // =========================

  @Test
  void shouldReturnReactionsByComment() {

    when(reactionRepository.findByCommentId(1L)).thenReturn(List.of(reaction));

    var result = service.getReactionsByComment(1L);

    assertEquals(1, result.size());
    assertEquals(1L, result.get(0).getCommentId());
  }

  // =========================
  // COUNT
  // =========================

  @Test
  void shouldCountReactions() {

    when(reactionRepository.findByCommentId(1L)).thenReturn(List.of(reaction, reaction));

    int count = service.countReactionsByComment(1L);

    assertEquals(2, count);
  }

  // =========================
  // REMOVE
  // =========================

  @Test
  void shouldRemoveReaction() {

    when(reactionRepository.findByUserIdAndCommentId(1L, 1L))
        .thenReturn(Optional.of(reaction));

    service.removeReaction(1L, 1L);

    verify(reactionRepository).delete(reaction);
  }

  @Test
  void shouldThrowExceptionWhenRemovingNonExistingReaction() {

    when(reactionRepository.findByUserIdAndCommentId(1L, 1L))
        .thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      service.removeReaction(1L, 1L);
    });
  }
}