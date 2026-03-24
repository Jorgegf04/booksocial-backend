package com.example.booksocial_backend.service;

import java.time.LocalDate;
import java.util.List;

import com.example.booksocial_backend.DTO.catalog.CreateWorkRequest;
import com.example.booksocial_backend.DTO.catalog.WorkDTO;

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

  WorkDTO createWork(CreateWorkRequest request);

  WorkDTO getWorkById(Long id);

  List<WorkDTO> getAllWorks();

  List<WorkDTO> searchWorksByTitle(String title);

  List<WorkDTO> getWorksByGenre(String genre);

  List<WorkDTO> getWorksByAuthor(Long authorId);

  List<WorkDTO> searchWorks(String title, String genre, Double rating);

  List<WorkDTO> getTopRatedWorks();

  List<WorkDTO> getWorksAfterDate(LocalDate date);

  WorkDTO updateWork(Long id, CreateWorkRequest request);

  void deleteWork(Long id);
}