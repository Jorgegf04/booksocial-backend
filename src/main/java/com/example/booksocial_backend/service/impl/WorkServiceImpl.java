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
import com.example.booksocial_backend.repository.AuthorFollowRepository;
import com.example.booksocial_backend.repository.AuthorRepository;
import com.example.booksocial_backend.repository.WorkRepository;
import com.example.booksocial_backend.service.EmailService;
import com.example.booksocial_backend.service.WorkService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkServiceImpl implements WorkService {

  private final WorkRepository workRepository;
  private final ModelMapper modelMapper;
  private final AuthorRepository authorRepository;
  private final AuthorFollowRepository authorFollowRepository;
  private final EmailService emailService;

  @Override
  public WorkResponseDTO createWork(WorkRequestDTO request) {

    validateWorkRequest(request);

    Work work = modelMapper.map(request, Work.class);

    work.setTitle(request.getTitle().trim());
    work.setGenre(request.getGenre());

    if (request.getAuthors() != null) {
      work.setAuthors(resolveAuthorsByName(request.getAuthors()));
    }

    Work saved = workRepository.save(work);

    List<Author> savedAuthors = saved.getAuthors();
    if (savedAuthors != null) {
      savedAuthors.forEach(author ->
          authorFollowRepository.findByAuthorId(author.getId()).forEach(follow -> {
            try {
              emailService.sendNewWorkNotification(
                  follow.getUser().getEmail(), author.getName(), saved.getTitle());
            } catch (Exception ignored) {}
          }));
    }

    return mapWorkToDTO(saved);
  }

  @Override
  public List<WorkResponseDTO> createMany(List<WorkRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista de obras no puede estar vacía");
    }

    List<Work> works = requests.stream()
        .map(request -> {

          validateWorkRequest(request);

          Work work = modelMapper.map(request, Work.class);

          work.setTitle(request.getTitle().trim());
          work.setGenre(request.getGenre());

          if (request.getAuthors() != null) {
            work.setAuthors(resolveAuthorsByName(request.getAuthors()));
          }

          return work;
        })
        .toList();

    return workRepository.saveAll(works)
        .stream()
        .map(this::mapWorkToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public WorkResponseDTO getWorkById(Long id) {
    return mapWorkToDTO(getWorkEntityById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkResponseDTO> getAllWorks() {
    return workRepository.findAll()
        .stream()
        .map(this::mapWorkToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkResponseDTO> searchWorksByTitle(String title) {
    return workRepository.findByTitleContainingIgnoreCase(title)
        .stream()
        .map(this::mapWorkToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkResponseDTO> getWorksByGenre(Genre genre) {
    return workRepository.findByGenre(genre)
        .stream()
        .map(this::mapWorkToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkResponseDTO> getWorksByAuthor(Long authorId) {
    return workRepository.findByAuthorId(authorId)
        .stream()
        .map(this::mapWorkToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkResponseDTO> searchWorks(String title, String genre, Double rating) {
    return workRepository.searchWorks(title, genre, rating)
        .stream()
        .map(this::mapWorkToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkResponseDTO> getTopRatedWorks() {
    return workRepository.findTopRatedWorks()
        .stream()
        .map(this::mapWorkToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkResponseDTO> getWorksAfterDate(LocalDate date) {
    return workRepository.findByPublicationDateAfter(date)
        .stream()
        .map(this::mapWorkToDTO)
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
      existing.setGenre(request.getGenre());
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

    if (request.getAuthors() != null) {
      existing.getAuthors().clear();
      existing.getAuthors().addAll(resolveAuthorsByName(request.getAuthors()));
    }

    validateWork(existing);

    return mapWorkToDTO(workRepository.save(existing));
  }

  @Override
  public void deleteWork(Long id) {
    workRepository.delete(getWorkEntityById(id));
  }

  // =========================
  // MAPPERS
  // =========================

  private WorkResponseDTO mapWorkToDTO(Work work) {

    WorkResponseDTO dto = new WorkResponseDTO();

    dto.setId(work.getId());
    dto.setTitle(work.getTitle());
    dto.setDescription(work.getDescription());
    dto.setGenre(work.getGenre());
    dto.setType(work.getType());
    dto.setDemographic(work.getDemographic());
    dto.setPublicationDate(work.getPublicationDate());
    dto.setImg(work.getImg());
    dto.setAverageRating(work.getAverageRating());

    List<Author> authors = work.getAuthors();
    dto.setAuthors(
        authors != null
            ? authors.stream().map(Author::getName).toList()
            : List.of());

    return dto;
  }

  // =========================
  // UTILIDADES
  // =========================

  private Work getWorkEntityById(Long id) {
    return workRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Obra no encontrada con id: " + id));
  }

  private List<Author> resolveAuthorsByName(List<String> authorNames) {
    return authorNames.stream()
        .map(name -> {
          String normalized = name.trim();
          return authorRepository.findByNameIgnoreCase(normalized)
              .orElseThrow(() -> new AuthorNotFoundException("Autor no encontrado: " + normalized));
        })
        .toList();
  }

  private void validateWorkRequest(WorkRequestDTO request) {

    if (request == null) {
      throw new IllegalArgumentException("La request no puede ser nula");
    }

    if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
      throw new IllegalArgumentException("El título es obligatorio");
    }

    if (request.getGenre() == null) {
      throw new IllegalArgumentException("El género es obligatorio");
    }
  }

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
    return workRepository.searchAdvanced(filter, pageable)
        .map(this::mapWorkToDTO);
  }
}