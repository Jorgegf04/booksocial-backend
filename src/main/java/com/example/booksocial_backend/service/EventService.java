package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.social.CreateEventRequest;
import com.example.booksocial_backend.DTO.social.EventDTO;

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

  EventDTO createEvent(CreateEventRequest request);

  EventDTO getEventById(Long id);

  List<EventDTO> getAllEvents();

  List<EventDTO> getAllEventsOrdered();

  List<EventDTO> getUpcomingEvents();

  EventDTO updateEvent(Long id, CreateEventRequest request);

  void deleteEvent(Long id);
}