package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.social.EventRequestDTO;
import com.example.booksocial_backend.DTO.social.EventResponseDTO;

/**
 * Servicio encargado de la gestión de eventos dentro del sistema BookSocial.
 *
 * Los eventos representan actividades exclusivas para usuarios suscritos,
 * permitiendo aumentar la interacción dentro de la plataforma.
 *
 * Este servicio proporciona operaciones de gestión, filtrado y consulta
 * de eventos, así como validaciones relacionadas con fechas y datos.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
public interface EventService {

  EventResponseDTO createEvent(EventRequestDTO request);

  EventResponseDTO getEventById(Long id);

  List<EventResponseDTO> getAllEvents();

  List<EventResponseDTO> getAllEventsOrdered();

  List<EventResponseDTO> getUpcomingEvents();

  EventResponseDTO updateEvent(Long id, EventRequestDTO request);

  void deleteEvent(Long id);

  EventResponseDTO joinEvent(Long eventId, Long userId);

  EventResponseDTO leaveEvent(Long eventId, Long userId);
}