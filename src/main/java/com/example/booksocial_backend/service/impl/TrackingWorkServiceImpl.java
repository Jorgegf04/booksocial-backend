package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.social.*;
import com.example.booksocial_backend.exception.TrackingWorkAlreadyExistsException;
import com.example.booksocial_backend.exception.TrackingWorkNotFoundException;
import com.example.booksocial_backend.exception.WorkNotFoundException;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.domain.social.TrackingWork;
import com.example.booksocial_backend.domain.social.TrackingWorkStatus;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.repository.TrackingOrderRepository;
import com.example.booksocial_backend.repository.TrackingWorkRepository;
import com.example.booksocial_backend.repository.WorkRepository;
import com.example.booksocial_backend.service.TrackingWorkService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link TrackingWorkService}.
 *
 * <p>
 * Gestiona la lógica de negocio asociada al seguimiento de obras por parte de
 * los usuarios,
 * incluyendo la creación de seguimientos, validaciones, control de duplicados y
 * consultas.
 * </p>
 *
 * <p>
 * Este servicio forma parte del sistema social de BookSocial y permite modelar
 * el estado de lectura de los usuarios sobre distintas obras.
 * </p>
 *
 * @author Jorge
 * @since 2026
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TrackingWorkServiceImpl implements TrackingWorkService {

  private static final Logger log = LoggerFactory.getLogger(TrackingWorkServiceImpl.class);

  private final TrackingWorkRepository repository; // ✔ CORRECTO
  private final WorkRepository workRepository;

  @Override
  public TrackingWorkResponseDTO create(TrackingWorkRequestDTO request) {

    validateRequest(request);
    log.info("[TRACKING_WORK] [CREATE] [START] userId={} workId={}", request.getUserId(), request.getWorkId());

    if (repository.existsByUserIdAndWorkId(request.getUserId(), request.getWorkId())) {
      throw new TrackingWorkAlreadyExistsException(request.getUserId(), request.getWorkId());
    }

    Work work = workRepository.findById(request.getWorkId())
        .orElseThrow(() -> new WorkNotFoundException("Obra no encontrada con id: " + request.getWorkId()));

    TrackingWork tracking = TrackingWork.builder()
        .user(User.builder().id(request.getUserId()).build())
        .work(work)
        .date(LocalDateTime.now())
        .status(
            request.getStatus() != null
                ? request.getStatus()
                : TrackingWorkStatus.PENDING)
        .build();

    return map(repository.save(tracking));
  }

  @Override
  public List<TrackingWorkResponseDTO> createMany(List<TrackingWorkRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista no puede estar vacía");
    }

    return requests.stream()
        .map(this::create)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<TrackingWorkResponseDTO> getByUser(Long userId) {

    return repository.findByUserId(userId)
        .stream()
        .map(this::map)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<TrackingWorkResponseDTO> getByWork(Long workId) {

    return repository.findByWorkId(workId)
        .stream()
        .map(this::map)
        .toList();
  }

  @Override
  public void delete(Long id) {

    log.info("[TRACKING_WORK] [DELETE] [START] id={}", id);
    repository.deleteById(id);
    log.info("[TRACKING_WORK] [DELETE] [SUCCESS] id={}", id);
  }

  @Override
  public TrackingWorkResponseDTO updateStatus(Long id, TrackingWorkRequestDTO request) {

    if (request == null || request.getStatus() == null) {
      throw new IllegalArgumentException("El estado es obligatorio");
    }

    log.info("[TRACKING_WORK] [UPDATE_STATUS] [START] id={} status={}", id, request.getStatus());
    TrackingWork tracking = repository.findById(id)
        .orElseThrow(() -> new TrackingWorkNotFoundException(id));

    tracking.setStatus(request.getStatus());

    return map(repository.save(tracking));
  }

  private TrackingWorkResponseDTO map(TrackingWork t) {
    return new TrackingWorkResponseDTO(
        t.getId(),

        t.getUser() != null ? t.getUser().getId() : null,
        t.getUser() != null ? t.getUser().getUsername() : null,

        t.getWork() != null ? t.getWork().getId() : null,
        t.getWork() != null ? t.getWork().getTitle() : null,

        t.getStatus(),
        t.getStatus() != null ? mapStatusLabel(t.getStatus()) : null,

        t.getDate(),

        t.getStatus() == TrackingWorkStatus.COMPLETED);
  }

  private String mapStatusLabel(TrackingWorkStatus status) {
    return switch (status) {
      case PENDING -> "Pendiente";
      case READING -> "Leyendo";
      case COMPLETED -> "Completado";
      case DROPPED -> "Abandonado";
    };
  }

  private void validateRequest(TrackingWorkRequestDTO request) {

    if (request == null) {
      throw new IllegalArgumentException("Request inválido");
    }

    if (request.getUserId() == null) {
      throw new IllegalArgumentException("UserId obligatorio");
    }

    if (request.getWorkId() == null) {
      throw new IllegalArgumentException("WorkId obligatorio");
    }
  }

}