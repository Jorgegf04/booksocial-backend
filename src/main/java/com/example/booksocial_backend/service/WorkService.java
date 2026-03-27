package com.example.booksocial_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.booksocial_backend.DTO.catalog.WorkFilterDTO;
import com.example.booksocial_backend.DTO.catalog.WorkRequestDTO;
import com.example.booksocial_backend.DTO.catalog.WorkResponseDTO;
import com.example.booksocial_backend.domain.catalog.Genre;

/**
 * Servicio encargado de la gestión del catálogo de obras.
 *
 * Proporciona funcionalidades avanzadas de búsqueda, filtrado
 * y organización del catálogo.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
public interface WorkService {

  WorkResponseDTO createWork(WorkRequestDTO request);

  List<WorkResponseDTO> createMany(List<WorkRequestDTO> requests);

  WorkResponseDTO getWorkById(Long id);

  List<WorkResponseDTO> getAllWorks();

  List<WorkResponseDTO> searchWorksByTitle(String title);

  List<WorkResponseDTO> getWorksByGenre(Genre genre);

  List<WorkResponseDTO> getWorksByAuthor(Long authorId);

  List<WorkResponseDTO> searchWorks(String title, String genre, Double rating);

  List<WorkResponseDTO> getTopRatedWorks();

  List<WorkResponseDTO> getWorksAfterDate(LocalDate date);

  WorkResponseDTO updateWork(Long id, WorkRequestDTO request);

  Page<WorkResponseDTO> searchAdvanced(WorkFilterDTO filter, Pageable pageable);

  void deleteWork(Long id);
}