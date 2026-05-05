package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
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

@SuppressWarnings("null")
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
        null,
        futureDate,
        List.of(1L));

    when(userRepository.findAllById(anyIterable())).thenReturn(List.of(user));
    when(eventRepository.save(any(Event.class))).thenReturn(event);

    var result = eventService.createEvent(request);

    assertEquals("Evento", result.getTitle());
    assertEquals(1, result.getTotalParticipants());
  }

  @Test
  void shouldCreateEventWithImgSuccessfully() {

    EventRequestDTO request = new EventRequestDTO(
        "Evento con foto",
        "Desc",
        "http://example.com/img.jpg",
        futureDate,
        null);

    Event eventWithImg = new Event();
    eventWithImg.setId(2L);
    eventWithImg.setTitle("Evento con foto");
    eventWithImg.setImg("http://example.com/img.jpg");
    eventWithImg.setDate(futureDate);
    eventWithImg.setUsers(new ArrayList<>());

    when(eventRepository.save(any(Event.class))).thenReturn(eventWithImg);

    var result = eventService.createEvent(request);

    assertEquals("Evento con foto", result.getTitle());
    assertEquals("http://example.com/img.jpg", result.getImg());
  }

  @Test
  void shouldThrowExceptionWhenTitleIsEmpty() {

    EventRequestDTO request = new EventRequestDTO(
        "   ",
        "Desc",
        null,
        futureDate,
        List.of(1L));

    assertThrows(IllegalArgumentException.class, () -> {
      eventService.createEvent(request);
    });

    verify(eventRepository, never()).save(any(Event.class));
  }

  @Test
  void shouldThrowExceptionWhenDateIsInPast() {

    EventRequestDTO request = new EventRequestDTO(
        "Evento",
        "Desc",
        null,
        LocalDateTime.now().minusDays(1),
        List.of(1L));

    assertThrows(IllegalArgumentException.class, () -> {
      eventService.createEvent(request);
    });
  }

  @Test
  void shouldCreateEventWithNoUsersWhenUserNotFound() {

    EventRequestDTO request = new EventRequestDTO(
        "Evento",
        "Desc",
        null,
        futureDate,
        List.of(99L));

    // findAllById devuelve lista vacía — usuario no existe
    when(userRepository.findAllById(anyIterable())).thenReturn(List.of());

    Event eventNoUsers = new Event();
    eventNoUsers.setId(2L);
    eventNoUsers.setTitle("Evento");
    eventNoUsers.setDate(futureDate);
    eventNoUsers.setUsers(new ArrayList<>());

    when(eventRepository.save(any(Event.class))).thenReturn(eventNoUsers);

    var result = eventService.createEvent(request);

    // El evento se crea igualmente pero sin participantes
    assertNotNull(result);
    assertEquals(0, result.getTotalParticipants());
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
        null,
        futureDate,
        List.of(1L));

    when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
    when(userRepository.findAllById(anyIterable())).thenReturn(List.of(user));
    when(eventRepository.save(any(Event.class))).thenReturn(event);

    var result = eventService.updateEvent(1L, request);

    assertEquals("Nuevo", result.getTitle());
  }

  @Test
  void shouldUpdateEventImgSuccessfully() {

    EventRequestDTO request = new EventRequestDTO(
        "Evento",
        "Desc",
        "http://new-img.com/photo.jpg",
        futureDate,
        null);

    when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
    when(eventRepository.save(any(Event.class))).thenReturn(event);

    var result = eventService.updateEvent(1L, request);

    assertNotNull(result);
    verify(eventRepository).save(any(Event.class));
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
