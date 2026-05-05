package com.example.booksocial_backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.social.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

  /**
   * Obtiene todos los eventos ordenados por fecha.
   */
  List<Event> findAllByOrderByDateAsc();

  /**
   * Obtiene los eventos futuros.
   *
   * @param date fecha actual
   * @return lista de eventos próximos
   */
  List<Event> findByDateAfter(LocalDateTime date);

  boolean existsByTitle(String title);

}