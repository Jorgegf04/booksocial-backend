package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.booksocial_backend.DTO.social.CommentRequestDTO;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.domain.social.Comment;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.repository.CommentRepository;
import com.example.booksocial_backend.repository.UserRepository;
import com.example.booksocial_backend.repository.WorkRepository;
import com.example.booksocial_backend.service.impl.CommentServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests unitarios de {@link com.example.booksocial_backend.service.impl.CommentServiceImpl}.
 *
 * <p>Verifica la lógica de negocio del sistema de comentarios jerárquicos:
 * creación de comentarios raíz, respuestas anidadas, validaciones y eliminación.
 * Usa Mockito para aislar la capa de servicio de la base de datos.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
class CommentServiceImplTest {

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private WorkRepository workRepository;

  @InjectMocks
  private CommentServiceImpl commentService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // =========================
  // CREATE COMMENT
  // =========================

  @Test
  void shouldCreateCommentSuccessfully() {

    CommentRequestDTO request = new CommentRequestDTO("Buen libro", 1L, 1L, null);

    User user = new User();
    user.setId(1L);
    user.setUsername("jorge");

    Work work = new Work();
    work.setId(1L);
    work.setTitle("Naruto");

    Comment saved = new Comment();
    saved.setId(1L);
    saved.setContent("Buen libro");
    saved.setDate(LocalDateTime.now());
    saved.setUser(user);
    saved.setWork(work);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(workRepository.findById(1L)).thenReturn(Optional.of(work));
    when(commentRepository.save(any())).thenReturn(saved);

    var result = commentService.createComment(request);

    assertNotNull(result);
    assertEquals("Buen libro", result.getContent());
  }

  @Test
  void shouldThrowExceptionWhenContentIsEmpty() {

    CommentRequestDTO request = new CommentRequestDTO("   ", 1L, 1L, null);

    User user = new User();
    user.setId(1L);

    Work work = new Work();
    work.setId(1L);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(workRepository.findById(1L)).thenReturn(Optional.of(work));

    assertThrows(IllegalArgumentException.class, () -> {
      commentService.createComment(request);
    });

    verify(commentRepository, never()).save(any());
  }

  // =========================
  // UPDATE
  // =========================

  @Test
  void shouldUpdateCommentSuccessfully() {

    User user = new User();
    user.setId(1L);

    Work work = new Work();
    work.setId(1L);
    work.setTitle("Naruto");

    Comment comment = new Comment();
    comment.setId(1L);
    comment.setContent("Viejo");
    comment.setUser(user);
    comment.setWork(work);

    CommentRequestDTO request = new CommentRequestDTO("Nuevo", 1L, 1L, null);

    when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
    when(commentRepository.save(comment)).thenReturn(comment);

    var result = commentService.updateComment(1L, request);

    assertEquals("Nuevo", result.getContent());
  }

  @Test
  void shouldThrowExceptionWhenUserNotOwner() {

    User user = new User();
    user.setId(2L);

    Work work = new Work();
    work.setId(1L);

    Comment comment = new Comment();
    comment.setId(1L);
    comment.setUser(user);
    comment.setWork(work);

    CommentRequestDTO request = new CommentRequestDTO("Nuevo", 1L, 1L, null);

    when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

    assertThrows(RuntimeException.class, () -> {
      commentService.updateComment(1L, request);
    });
  }

  // =========================
  // DELETE
  // =========================

  @Test
  void shouldDeleteComment() {

    Comment comment = new Comment();
    comment.setId(1L);

    when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

    commentService.deleteComment(1L);

    verify(commentRepository).delete(comment);
  }
}