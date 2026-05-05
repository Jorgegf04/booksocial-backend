package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.social.EventRequestDTO;
import com.example.booksocial_backend.DTO.social.EventResponseDTO;
import com.example.booksocial_backend.domain.social.Event;
import com.example.booksocial_backend.domain.user.User;

import com.example.booksocial_backend.repository.EventRepository;
import com.example.booksocial_backend.repository.UserRepository;
import com.example.booksocial_backend.exception.UserNotFoundException;
import com.example.booksocial_backend.service.EventService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link EventService}.
 *
 * Gestiona la lógica de negocio relacionada con los eventos exclusivos,
 * incluyendo validaciones de fechas, organización temporal y recuperación
 * de eventos futuros.
 *
 * Evita exponer entidades del dominio directamente mediante el uso de DTOs.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.1
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

  private final EventRepository eventRepository;
  private final UserRepository userRepository;

  @Override
  public EventResponseDTO createEvent(EventRequestDTO request) {

    Event event = new Event();

    event.setTitle(request.getTitle().trim());
    event.setDescription(request.getDescription());
    event.setImg(request.getImg());
    event.setDate(request.getDate());

    // ✔ CORRECTO: lista mutable + usuarios reales
    if (request.getUserIds() != null && !request.getUserIds().isEmpty()) {
      List<User> users = userRepository.findAllById(request.getUserIds());
      event.setUsers(new ArrayList<>(users));
    }

    validateEvent(event);

    if (!event.getDate().isAfter(LocalDateTime.now())) {
      throw new IllegalArgumentException("La fecha del evento debe ser futura");
    }

    Event saved = eventRepository.save(event);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public EventResponseDTO getEventById(Long id) {
    return mapToDTO(getEventEntityById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventResponseDTO> getAllEvents() {

    return eventRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventResponseDTO> getAllEventsOrdered() {

    return eventRepository.findAllByOrderByDateAsc()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventResponseDTO> getUpcomingEvents() {

    return eventRepository.findByDateAfter(LocalDateTime.now())
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public EventResponseDTO updateEvent(Long id, EventRequestDTO request) {

    Event existing = getEventEntityById(id);

    existing.setTitle(request.getTitle().trim());
    existing.setDescription(request.getDescription());
    if (request.getImg() != null) existing.setImg(request.getImg());
    existing.setDate(request.getDate());

    // ✔ CORRECTO: NO reemplazar lista → modificarla
    if (request.getUserIds() != null) {

      List<User> users = userRepository.findAllById(request.getUserIds());

      existing.getUsers().clear();
      existing.getUsers().addAll(users);
    }

    validateEvent(existing);

    if (!existing.getDate().isAfter(LocalDateTime.now())) {
      throw new IllegalArgumentException("La fecha del evento debe ser futura");
    }

    return mapToDTO(eventRepository.save(existing));
  }

  @Override
  public void deleteEvent(Long id) {

    eventRepository.delete(getEventEntityById(id));
  }

  private EventResponseDTO mapToDTO(Event event) {

    List<User> safeUsers = event.getUsers() != null ? event.getUsers() : List.of();

    List<Long> userIds = safeUsers.stream()
        .map(User::getId)
        .toList();

    List<String> usernames = safeUsers.stream()
        .map(User::getUsername)
        .toList();

    return new EventResponseDTO(
        event.getId(),
        event.getTitle(),
        event.getDescription(),
        event.getImg(),
        event.getDate(),
        userIds,
        usernames,
        userIds.size());
  }

  @Override
  public EventResponseDTO joinEvent(Long eventId, Long userId) {
    Event event = getEventEntityById(eventId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    List<User> users = event.getUsers() != null ? event.getUsers() : new ArrayList<>();
    if (event.getUsers() == null) event.setUsers(users);
    boolean alreadyJoined = users.stream().anyMatch(u -> u.getId().equals(userId));
    if (alreadyJoined) {
      throw new IllegalArgumentException("El usuario ya está inscrito en este evento");
    }
    users.add(user);
    return mapToDTO(eventRepository.save(event));
  }

  @Override
  public EventResponseDTO leaveEvent(Long eventId, Long userId) {
    Event event = getEventEntityById(eventId);
    if (event.getUsers() == null) {
      throw new IllegalArgumentException("El usuario no está inscrito en este evento");
    }
    boolean removed = event.getUsers().removeIf(u -> u.getId().equals(userId));
    if (!removed) {
      throw new IllegalArgumentException("El usuario no está inscrito en este evento");
    }
    return mapToDTO(eventRepository.save(event));
  }

  private Event getEventEntityById(Long id) {
    return eventRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado con id: " + id));
  }

  /**
   * Valida los datos básicos de un evento.
   */
  private void validateEvent(Event event) {

    if (event == null) {
      throw new IllegalArgumentException("El evento no puede ser nulo");
    }

    if (event.getTitle() == null || event.getTitle().trim().isEmpty()) {
      throw new IllegalArgumentException("El título del evento es obligatorio");
    }

    if (event.getDate() == null) {
      throw new IllegalArgumentException("La fecha del evento es obligatoria");
    }
  }
}