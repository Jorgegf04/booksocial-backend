package com.example.booksocial_backend.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.exception.AuthorNotFoundException;
import com.example.booksocial_backend.DTO.catalog.WorkFilterDTO;
import com.example.booksocial_backend.DTO.catalog.WorkRequestDTO;
import com.example.booksocial_backend.DTO.catalog.WorkResponseDTO;
import com.example.booksocial_backend.domain.catalog.Author;
import com.example.booksocial_backend.domain.catalog.Genre;
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
  public WorkResponseDTO createWork(WorkRequestDTO request) {

    if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
      throw new IllegalArgumentException("El título es obligatorio");
    }

    if (request.getGenre() == null) {
      throw new IllegalArgumentException("El género es obligatorio");
    }

    Work work = modelMapper.map(request, Work.class);

    work.setTitle(request.getTitle().trim());
    work.setGenre(request.getGenre());

    if (request.getAuthorIds() != null) {
      work.setAuthors(
          request.getAuthorIds().stream()
              .map(id -> authorRepository.findById(id)
                  .orElseThrow(() -> new AuthorNotFoundException("Autor no encontrado: " + id)))
              .toList());
    }

    Work saved = workRepository.save(work);
    return mapToDTO(saved);
  }

  @Override
  public List<WorkResponseDTO> createMany(List<WorkRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista de obras no puede estar vacía");
    }

    List<Work> works = requests.stream()
        .map(request -> {

          if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio");
          }

          if (request.getGenre() == null) {
            throw new IllegalArgumentException("El género es obligatorio");
          }

          Work work = modelMapper.map(request, Work.class);

          work.setTitle(request.getTitle().trim());
          work.setGenre(request.getGenre());

          if (request.getAuthorIds() != null) {
            work.setAuthors(
                request.getAuthorIds().stream()
                    .map(id -> authorRepository.findById(id)
                        .orElseThrow(() -> new AuthorNotFoundException("Autor no encontrado: " + id)))
                    .toList());
          }

          return work;
        })
        .toList();

    return workRepository.saveAll(works)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public WorkResponseDTO getWorkById(Long id) {

    Work work = getWorkEntityById(id);

    return mapToDTO(work);
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkResponseDTO> getAllWorks() {

    return workRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkResponseDTO> searchWorksByTitle(String title) {

    return workRepository.findByTitleContainingIgnoreCase(title)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkResponseDTO> getWorksByGenre(Genre genre) {
    return workRepository.findByGenre(genre)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkResponseDTO> getWorksByAuthor(Long authorId) {

    return workRepository.findByAuthorId(authorId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkResponseDTO> searchWorks(String title, String genre, Double rating) {

    return workRepository.searchWorks(title, genre, rating)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkResponseDTO> getTopRatedWorks() {

    return workRepository.findTopRatedWorks()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkResponseDTO> getWorksAfterDate(LocalDate date) {

    return workRepository.findByPublicationDateAfter(date)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public WorkResponseDTO updateWork(Long id, WorkRequestDTO request) {

    Work existing = getWorkEntityById(id);

    if (request.getTitle() != null) {
      existing.setTitle(request.getTitle().trim());
    }

    if (request.getDescription() != null) {
      existing.setDescription(request.getDescription());
    }

    if (request.getGenre() != null) {
      existing.setGenre(request.getGenre()); // ✅ sin trim
    }

    if (request.getPublicationDate() != null) {
      existing.setPublicationDate(request.getPublicationDate());
    }

    if (request.getImg() != null) {
      existing.setImg(request.getImg());
    }

    if (request.getAverageRating() != null) {
      existing.setAverageRating(request.getAverageRating());
    }

    if (request.getAuthorIds() != null) {
      existing.getAuthors().clear();

      existing.getAuthors().addAll(
          request.getAuthorIds().stream()
              .map(aid -> authorRepository.findById(aid)
                  .orElseThrow(() -> new AuthorNotFoundException("Autor no encontrado: " + aid)))
              .toList());
    }

    validateWork(existing);

    return mapToDTO(workRepository.save(existing));
  }

  @Override
  public void deleteWork(Long id) {

    Work work = getWorkEntityById(id);

    workRepository.delete(work);
  }

  /**
   * Convierte Work → WorkDTO.
   */
  private WorkResponseDTO mapToDTO(Work work) {

    return new WorkResponseDTO(
        work.getId(),
        work.getTitle(),
        work.getDescription(),
        work.getGenre(),
        work.getType(),
        work.getDemographic(),
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

    if (work.getGenre() == null) {
      throw new IllegalArgumentException("El género es obligatorio");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Page<WorkResponseDTO> searchAdvanced(WorkFilterDTO filter, Pageable pageable) {

    System.out.println(filter);
    return workRepository.searchAdvanced(filter, pageable)
        .map(this::mapToDTO);
  }
}