package com.example.booksocial_backend.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.DTO.catalog.CreateWorkRequest;
import com.example.booksocial_backend.DTO.catalog.WorkDTO;
import com.example.booksocial_backend.domain.catalog.Author;
import com.example.booksocial_backend.repository.AuthorRepository;
import com.example.booksocial_backend.repository.WorkRepository;
import com.example.booksocial_backend.service.WorkService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link WorkService}.
 *
 * Gestiona la lógica de negocio del catálogo, incluyendo
 * búsquedas avanzadas, filtrado y ranking de obras.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class WorkServiceImpl implements WorkService {

  private final WorkRepository workRepository;
  private final ModelMapper modelMapper;
  private final AuthorRepository authorRepository;

  @Override
  public WorkDTO createWork(CreateWorkRequest request) {

    Work work = modelMapper.map(request, Work.class);

    // Set manual autores
    if (request.authorIds() != null) {
      work.setAuthors(
          request.authorIds().stream()
              .map(id -> authorRepository.findById(id)
                  .orElseThrow(() -> new RuntimeException("Autor no encontrado: " + id)))
              .toList());
    }

    validateWork(work);

    work.setTitle(work.getTitle().trim());
    work.setGenre(work.getGenre().trim());

    Work saved = workRepository.save(work);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public WorkDTO getWorkById(Long id) {

    Work work = getWorkEntityById(id);

    return mapToDTO(work);
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkDTO> getAllWorks() {

    return workRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkDTO> searchWorksByTitle(String title) {

    return workRepository.findByTitleContainingIgnoreCase(title)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkDTO> getWorksByGenre(String genre) {

    return workRepository.findByGenreIgnoreCase(genre)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkDTO> getWorksByAuthor(Long authorId) {

    return workRepository.findByAuthorId(authorId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkDTO> searchWorks(String title, String genre, Double rating) {

    return workRepository.searchWorks(title, genre, rating)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkDTO> getTopRatedWorks() {

    return workRepository.findTopRatedWorks()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkDTO> getWorksAfterDate(LocalDate date) {

    return workRepository.findByPublicationDateAfter(date)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public WorkDTO updateWork(Long id, CreateWorkRequest request) {

    Work existing = getWorkEntityById(id);

    Work updated = modelMapper.map(request, Work.class);

    if (request.authorIds() != null) {
      updated.setAuthors(
          request.authorIds().stream()
              .map(aid -> authorRepository.findById(aid)
                  .orElseThrow(() -> new RuntimeException("Autor no encontrado: " + aid)))
              .toList());
    }

    validateWork(updated);

    existing.setTitle(updated.getTitle().trim());
    existing.setDescription(updated.getDescription());
    existing.setGenre(updated.getGenre().trim());
    existing.setPublicationDate(updated.getPublicationDate());
    existing.setImg(updated.getImg());
    existing.setAverageRating(updated.getAverageRating());
    existing.setAuthors(updated.getAuthors());

    Work saved = workRepository.save(existing);

    return mapToDTO(saved);
  }

  @Override
  public void deleteWork(Long id) {

    Work work = getWorkEntityById(id);

    workRepository.delete(work);
  }

  /**
   * Convierte Work → WorkDTO.
   */
  private WorkDTO mapToDTO(Work work) {

    return new WorkDTO(
        work.getId(),
        work.getTitle(),
        work.getDescription(),
        work.getGenre(),
        work.getPublicationDate(),
        work.getImg(),
        work.getAverageRating(),
        work.getAuthors()
            .stream()
            .map(Author::getId)
            .toList());
  }

  private Work getWorkEntityById(Long id) {

    return workRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Obra no encontrada con id: " + id));
  }

  /**
   * Valida los datos de una obra.
   */
  private void validateWork(Work work) {

    if (work == null) {
      throw new IllegalArgumentException("La obra no puede ser nula");
    }

    if (work.getTitle() == null || work.getTitle().trim().isEmpty()) {
      throw new IllegalArgumentException("El título es obligatorio");
    }

    if (work.getGenre() == null || work.getGenre().trim().isEmpty()) {
      throw new IllegalArgumentException("El género es obligatorio");
    }
  }
}