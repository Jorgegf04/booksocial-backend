package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.booksocial_backend.DTO.social.EventRequestDTO;
import com.example.booksocial_backend.domain.social.Event;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.repository.EventRepository;
import com.example.booksocial_backend.repository.UserRepository;
import com.example.booksocial_backend.service.impl.EventServiceImpl;

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
class EventServiceImplTest {

  @Mock
  private EventRepository eventRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private EventServiceImpl eventService;

  private Event event;
  private User user;
  private LocalDateTime futureDate;

  @BeforeEach
  void setUp() {

    futureDate = LocalDateTime.of(2030, 1, 1, 12, 0);

    user = new User();
    user.setId(1L);
    user.setUsername("jorge");

    event = new Event();
    event.setId(1L);
    event.setTitle("Evento");
    event.setDescription("Desc");
    event.setDate(futureDate);
    event.setUsers(new ArrayList<>(List.of(user)));
  }

  // =========================
  // CREATE
  // =========================

  @Test
  void shouldCreateEventSuccessfully() {

    EventRequestDTO request = new EventRequestDTO(
        "Evento",
        "Desc",
        futureDate,
        List.of(1L));

    when(eventRepository.save(any())).thenReturn(event);

    var result = eventService.createEvent(request);

    assertEquals("Evento", result.getTitle());
    assertEquals(1, result.getTotalParticipants());
  }

  @Test
  void shouldThrowExceptionWhenTitleIsEmpty() {

    EventRequestDTO request = new EventRequestDTO(
        "   ",
        "Desc",
        futureDate,
        List.of(1L));

    assertThrows(IllegalArgumentException.class, () -> {
      eventService.createEvent(request);
    });

    verify(eventRepository, never()).save(any());
  }

  @Test
  void shouldThrowExceptionWhenDateIsInPast() {

    EventRequestDTO request = new EventRequestDTO(
        "Evento",
        "Desc",
        LocalDateTime.now().minusDays(1),
        List.of(1L));

    assertThrows(IllegalArgumentException.class, () -> {
      eventService.createEvent(request);
    });
  }

  @Test
  void shouldThrowExceptionWhenUserNotFound() {

    EventRequestDTO request = new EventRequestDTO(
        "Evento",
        "Desc",
        futureDate,
        List.of(1L));

    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      eventService.createEvent(request);
    });
  }

  // =========================
  // GET
  // =========================

  @Test
  void shouldGetEventById() {

    when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

    var result = eventService.getEventById(1L);

    assertEquals("Evento", result.getTitle());
    assertEquals(1, result.getTotalParticipants());
  }

  @Test
  void shouldThrowExceptionWhenEventNotFound() {

    when(eventRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      eventService.getEventById(1L);
    });
  }

  @Test
  void shouldReturnAllEvents() {

    when(eventRepository.findAll()).thenReturn(List.of(event));

    var result = eventService.getAllEvents();

    assertEquals(1, result.size());
    assertEquals("Evento", result.get(0).getTitle());
  }

  // =========================
  // UPDATE
  // =========================

  @Test
  void shouldUpdateEventSuccessfully() {

    EventRequestDTO request = new EventRequestDTO(
        "Nuevo",
        "Nueva desc",
        futureDate,
        List.of(1L));

    when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(eventRepository.save(any())).thenReturn(event);

    var result = eventService.updateEvent(1L, request);

    assertEquals("Nuevo", result.getTitle());
  }

  // =========================
  // DELETE
  // =========================

  @Test
  void shouldDeleteEvent() {

    when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

    eventService.deleteEvent(1L);

    verify(eventRepository).delete(event);
  }

  @Test
  void shouldThrowExceptionWhenDeletingNonExistingEvent() {

    when(eventRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      eventService.deleteEvent(1L);
    });
  }
}