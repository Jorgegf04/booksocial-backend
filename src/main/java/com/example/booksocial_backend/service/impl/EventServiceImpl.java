package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.social.CreateEventRequest;
import com.example.booksocial_backend.DTO.social.EventDTO;
import com.example.booksocial_backend.domain.social.Event;
import com.example.booksocial_backend.domain.user.User;

import com.example.booksocial_backend.repository.EventRepository;
import com.example.booksocial_backend.service.EventService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link EventService}.
 *
 * Gestiono la lógica de negocio relacionada con los eventos exclusivos,
 * incluyendo validaciones de fechas, organización temporal y recuperación
 * de eventos futuros.
 *
 * Evito exponer entidades del dominio directamente mediante el uso de DTOs.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

  private final EventRepository eventRepository;

  @Override
  public EventDTO createEvent(CreateEventRequest request) {

    Event event = new Event();

    event.setTitle(request.title().trim());
    event.setDescription(request.description());
    event.setDate(request.date());

    // Mapear usuarios
    if (request.userIds() != null) {
      event.setUsers(
          request.userIds().stream()
              .map(id -> User.builder().id(id).build())
              .toList());
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
  public EventDTO getEventById(Long id) {

    return mapToDTO(getEventEntityById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventDTO> getAllEvents() {

    return eventRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventDTO> getAllEventsOrdered() {

    return eventRepository.findAllByOrderByDateAsc()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventDTO> getUpcomingEvents() {

    return eventRepository.findByDateAfter(LocalDateTime.now())
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public EventDTO updateEvent(Long id, CreateEventRequest request) {

    Event existing = getEventEntityById(id);

    existing.setTitle(request.title().trim());
    existing.setDescription(request.description());
    existing.setDate(request.date());

    if (request.userIds() != null) {
      existing.setUsers(
          request.userIds().stream()
              .map(uid -> User.builder().id(uid).build())
              .toList());
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

  private EventDTO mapToDTO(Event event) {

    return new EventDTO(
        event.getId(),
        event.getTitle(),
        event.getDescription(),
        event.getDate(),
        event.getUsers().stream()
            .map(User::getId)
            .toList());
  }

  private Event getEventEntityById(Long id) {

    return eventRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + id));
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