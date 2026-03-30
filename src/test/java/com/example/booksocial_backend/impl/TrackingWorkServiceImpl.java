package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.booksocial_backend.DTO.social.TrackingWorkRequestDTO;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.domain.social.TrackingWork;
import com.example.booksocial_backend.domain.social.TrackingWorkStatus;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.repository.TrackingWorkRepository;
import com.example.booksocial_backend.repository.WorkRepository;
import com.example.booksocial_backend.service.impl.TrackingWorkServiceImpl;

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
class TrackingWorkServiceImplTest {

  @Mock
  private TrackingWorkRepository repository;

  @Mock
  private WorkRepository workRepository;

  @InjectMocks
  private TrackingWorkServiceImpl service;

  private TrackingWork tracking;
  private Work work;

  @BeforeEach
  void setUp() {

    User user = new User();
    user.setId(1L);
    user.setUsername("jorge");

    work = new Work();
    work.setId(1L);
    work.setTitle("Naruto");

    tracking = TrackingWork.builder()
        .id(1L)
        .user(user)
        .work(work)
        .status(TrackingWorkStatus.PENDING)
        .date(LocalDateTime.now())
        .build();
  }

  // =========================
  // CREATE
  // =========================

  @Test
  void shouldCreateTrackingSuccessfully() {

    TrackingWorkRequestDTO request = new TrackingWorkRequestDTO(1L, 1L, null);

    when(repository.existsByUserIdAndWorkId(1L, 1L)).thenReturn(false);
    when(workRepository.findById(1L)).thenReturn(Optional.of(work));
    when(repository.save(any())).thenReturn(tracking);

    var result = service.create(request);

    assertEquals(1L, result.getUserId());
    assertEquals("Naruto", result.getWorkTitle());
    assertEquals(TrackingWorkStatus.PENDING, result.getStatus());
  }

  @Test
  void shouldThrowExceptionWhenDuplicateTracking() {

    TrackingWorkRequestDTO request = new TrackingWorkRequestDTO(1L, 1L, null);

    when(repository.existsByUserIdAndWorkId(1L, 1L)).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> {
      service.create(request);
    });
  }

  @Test
  void shouldThrowExceptionWhenWorkNotFound() {

    TrackingWorkRequestDTO request = new TrackingWorkRequestDTO(1L, 1L, null);

    when(repository.existsByUserIdAndWorkId(1L, 1L)).thenReturn(false);
    when(workRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      service.create(request);
    });
  }

  // =========================
  // CREATE MANY
  // =========================

  @Test
  void shouldCreateManyTracking() {

    TrackingWorkRequestDTO request = new TrackingWorkRequestDTO(1L, 1L, null);

    when(repository.existsByUserIdAndWorkId(any(), any())).thenReturn(false);
    when(workRepository.findById(1L)).thenReturn(Optional.of(work));
    when(repository.save(any())).thenReturn(tracking);

    var result = service.createMany(List.of(request, request));

    assertEquals(2, result.size());
  }

  @Test
  void shouldThrowExceptionWhenCreateManyEmpty() {

    assertThrows(IllegalArgumentException.class, () -> {
      service.createMany(List.of());
    });
  }

  // =========================
  // READ
  // =========================

  @Test
  void shouldGetByUser() {

    when(repository.findByUserId(1L)).thenReturn(List.of(tracking));

    var result = service.getByUser(1L);

    assertEquals(1, result.size());
  }

  @Test
  void shouldGetByWork() {

    when(repository.findByWorkId(1L)).thenReturn(List.of(tracking));

    var result = service.getByWork(1L);

    assertEquals(1, result.size());
  }

  // =========================
  // UPDATE
  // =========================

  @Test
  void shouldUpdateStatus() {

    TrackingWorkRequestDTO request = new TrackingWorkRequestDTO(1L, 1L, TrackingWorkStatus.READING);

    when(repository.findById(1L)).thenReturn(Optional.of(tracking));
    when(repository.save(any())).thenReturn(tracking);

    var result = service.updateStatus(1L, request);

    assertEquals(TrackingWorkStatus.READING, result.getStatus());
  }

  @Test
  void shouldThrowExceptionWhenStatusNull() {

    TrackingWorkRequestDTO request = new TrackingWorkRequestDTO(1L, 1L, null);

    assertThrows(IllegalArgumentException.class, () -> {
      service.updateStatus(1L, request);
    });
  }

  // =========================
  // DELETE
  // =========================

  @Test
  void shouldDeleteTracking() {

    service.delete(1L);

    verify(repository).deleteById(1L);
  }
}