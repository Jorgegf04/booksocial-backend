package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.catalog.AuthorRequestDTO;
import com.example.booksocial_backend.DTO.catalog.AuthorResponseDTO;
import com.example.booksocial_backend.DTO.catalog.WorkResponseDTO;
import com.example.booksocial_backend.DTO.user.UserResponseDTO;
import com.example.booksocial_backend.domain.catalog.Author;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.domain.social.AuthorFollow;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.exception.AuthorAlreadyExistsException;
import com.example.booksocial_backend.exception.AuthorNotFoundException;
import com.example.booksocial_backend.exception.UserNotFoundException;
import com.example.booksocial_backend.repository.AuthorFollowRepository;
import com.example.booksocial_backend.repository.AuthorRepository;
import com.example.booksocial_backend.repository.UserRepository;
import com.example.booksocial_backend.repository.WorkRepository;
import com.example.booksocial_backend.service.AuthorService;
import com.example.booksocial_backend.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository authorRepository;
  private final AuthorFollowRepository authorFollowRepository;
  private final UserRepository userRepository;
  private final WorkRepository workRepository;
  private final EmailService emailService;

  @Override
  public AuthorResponseDTO createAuthor(AuthorRequestDTO request) {

    validateAuthorRequest(request);

    String normalizedName = request.getName().trim();

    if (authorRepository.existsByName(normalizedName)) {
      throw new AuthorAlreadyExistsException(normalizedName);
    }

    Author author = Author.builder()
        .name(normalizedName)
        .nationality(request.getNationality())
        .birthDate(request.getBirthDate())
        .img(request.getImg())
        .build();

    Author saved = authorRepository.save(author);
    Long savedId = saved.getId();

    if (savedId != null && request.getWorkIds() != null && !request.getWorkIds().isEmpty()) {
      for (Long workId : request.getWorkIds()) {
        workRepository.findById(workId).ifPresent(w -> {
          if (w.getAuthors().stream().noneMatch(a -> savedId.equals(a.getId()))) {
            w.getAuthors().add(saved);
            workRepository.save(w);
          }
        });
      }
    }

    return mapAuthorToDTO(savedId != null
        ? authorRepository.findById(savedId).orElse(saved) : saved);
  }

  @Override
  @Transactional(readOnly = true)
  public AuthorResponseDTO getAuthorById(Long id) {
    return mapAuthorToDTO(getAuthorEntityById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> getAllAuthors() {
    return authorRepository.findAll()
        .stream()
        .map(this::mapAuthorToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> searchAuthorsByName(String name) {
    return authorRepository.findByNameContainingIgnoreCase(name)
        .stream()
        .map(this::mapAuthorToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> getAuthorsOrderedByName() {
    return authorRepository.findAllOrderByName()
        .stream()
        .map(this::mapAuthorToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> getAuthorsWithWorks() {
    return authorRepository.findAuthorsWithWorks()
        .stream()
        .map(this::mapAuthorToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorResponseDTO> getTopAuthors() {
    return authorRepository.findTopAuthorsByWorksCount()
        .stream()
        .map(this::mapAuthorToDTO)
        .toList();
  }

  @Override
  public AuthorResponseDTO updateAuthor(Long id, AuthorRequestDTO request) {

    Author existing = getAuthorEntityById(id);

    String normalizedName = request.getName().trim();

    if (!existing.getName().equalsIgnoreCase(normalizedName)
        && authorRepository.existsByName(normalizedName)) {
      throw new IllegalArgumentException("Ya existe otro autor con ese nombre");
    }

    existing.setName(normalizedName);
    existing.setNationality(request.getNationality());
    existing.setBirthDate(request.getBirthDate());
    if (request.getImg() != null) existing.setImg(request.getImg());

    Author saved = authorRepository.save(existing);

    if (request.getWorkIds() != null) {
      syncAuthorWorkAssociations(saved, request.getWorkIds());
    }

    Long savedId = saved.getId();
    return mapAuthorToDTO(savedId != null
        ? authorRepository.findById(savedId).orElse(saved) : saved);
  }

  @Override
  public void deleteAuthor(Long id) {
    authorRepository.delete(getAuthorEntityById(id));
  }

  @Override
  public void followAuthor(Long userId, Long authorId) {
    if (authorFollowRepository.existsByUserIdAndAuthorId(userId, authorId)) return;

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + userId));
    Author author = getAuthorEntityById(authorId);

    authorFollowRepository.save(AuthorFollow.builder()
        .user(user)
        .author(author)
        .followDate(LocalDateTime.now())
        .build());

    emailService.sendFollowConfirmation(user.getEmail(), author.getName());
  }

  @Override
  @Transactional
  public void unfollowAuthor(Long userId, Long authorId) {
    authorFollowRepository.deleteByUserIdAndAuthorId(userId, authorId);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isFollowing(Long userId, Long authorId) {
    return authorFollowRepository.existsByUserIdAndAuthorId(userId, authorId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponseDTO> getFollowers(Long authorId) {
    return authorFollowRepository.findByAuthorId(authorId).stream()
        .filter(af -> af.getUser() != null)
        .map(af -> {
          User u = af.getUser();
          return new UserResponseDTO(u.getId(), u.getUsername(), u.getEmail(),
              u.getName(), u.getSecondName(), u.getImg(),
              u.getRegistrationDate(), u.getActive(), u.getRole());
        })
        .toList();
  }

  // =========================
  // MAPPERS
  // =========================

  private AuthorResponseDTO mapAuthorToDTO(Author author) {
    return new AuthorResponseDTO(
        author.getId(),
        author.getName(),
        author.getNationality(),
        author.getBirthDate(),
        author.getImg(),
        authorFollowRepository.countByAuthorId(author.getId()),
        author.getWorks().stream()
            .map(this::mapWorkToDTO)
            .toList());
  }

  private WorkResponseDTO mapWorkToDTO(Work work) {
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
            .map(Author::getName)
            .toList());
  }

  // =========================
  // UTILIDADES
  // =========================

  private void syncAuthorWorkAssociations(Author author, List<Long> newWorkIds) {
    Long authorId = author.getId();
    if (authorId == null) return;

    List<Work> currentWorks = workRepository.findByAuthorId(authorId);
    Set<Long> newIds = new HashSet<>(newWorkIds);
    Set<Long> currentIds = new HashSet<>();
    currentWorks.forEach(w -> currentIds.add(w.getId()));

    currentWorks.stream()
        .filter(w -> !newIds.contains(w.getId()))
        .forEach(w -> {
          w.getAuthors().removeIf(a -> authorId.equals(a.getId()));
          workRepository.save(w);
        });

    newIds.stream()
        .filter(id -> !currentIds.contains(id))
        .forEach(id -> workRepository.findById(id).ifPresent(w -> {
          if (w.getAuthors().stream().noneMatch(a -> authorId.equals(a.getId()))) {
            w.getAuthors().add(author);
            workRepository.save(w);
          }
        }));
  }

  private Author getAuthorEntityById(Long id) {
    return authorRepository.findById(id)
        .orElseThrow(() -> new AuthorNotFoundException("Autor no encontrado con id: " + id));
  }

  private void validateAuthorRequest(AuthorRequestDTO request) {
    if (request == null) {
      throw new IllegalArgumentException("La request no puede ser nula");
    }
    if (request.getName() == null || request.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre del autor es obligatorio");
    }
  }
}
