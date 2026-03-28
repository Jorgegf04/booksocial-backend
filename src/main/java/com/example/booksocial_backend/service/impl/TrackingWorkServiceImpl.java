package com.example.booksocial_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.social.*;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.domain.social.TrackingWork;
import com.example.booksocial_backend.domain.social.TrackingWorkStatus;
import com.example.booksocial_backend.domain.user.User;
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

  private final TrackingWorkRepository repository;
  private final WorkRepository workRepository;

  /**
   * Crea un nuevo seguimiento de obra.
   *
   * <p>
   * Valida los datos de entrada, comprueba que no exista un seguimiento duplicado
   * y carga la obra real desde la base de datos para garantizar consistencia.
   * </p>
   *
   * @param request DTO con los datos del seguimiento
   * @return DTO con la información del seguimiento creado
   */
  @Override
  public TrackingWorkResponseDTO create(TrackingWorkRequestDTO request) {

    validateRequest(request);

    if (repository.existsByUserIdAndWorkId(request.getUserId(), request.getWorkId())) {
      throw new IllegalArgumentException("El usuario ya sigue esta obra");
    }

    Work work = workRepository.findById(request.getWorkId())
        .orElseThrow(() -> new RuntimeException("Obra no encontrada con id: " + request.getWorkId()));

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

  /**
   * Crea múltiples seguimientos de obras en una sola operación.
   *
   * @param requests lista de solicitudes de seguimiento
   * @return lista de seguimientos creados
   */
  @Override
  public List<TrackingWorkResponseDTO> createMany(List<TrackingWorkRequestDTO> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("La lista no puede estar vacía");
    }

    return requests.stream()
        .map(this::create)
        .toList();
  }

  /**
   * Obtiene todas las obras seguidas por un usuario.
   *
   * @param userId identificador del usuario
   * @return lista de seguimientos
   */
  @Override
  @Transactional(readOnly = true)
  public List<TrackingWorkResponseDTO> getByUser(Long userId) {

    return repository.findByUserId(userId)
        .stream()
        .map(this::map)
        .toList();
  }

  /**
   * Obtiene todos los usuarios que siguen una obra.
   *
   * @param workId identificador de la obra
   * @return lista de seguimientos
   */
  @Override
  @Transactional(readOnly = true)
  public List<TrackingWorkResponseDTO> getByWork(Long workId) {

    return repository.findByWorkId(workId)
        .stream()
        .map(this::map)
        .toList();
  }

  /**
   * Elimina un seguimiento por su ID.
   *
   * @param id identificador del seguimiento
   */
  @Override
  public void delete(Long id) {

    repository.deleteById(id);
  }

  /**
   * Convierte la entidad TrackingWork en DTO de respuesta.
   *
   * @param t entidad TrackingWork
   * @return DTO de salida
   */
  private TrackingWorkResponseDTO map(TrackingWork t) {

    return new TrackingWorkResponseDTO(
        t.getId(),
        t.getUser().getId(),
        t.getWork().getId(),
        t.getWork().getTitle(),
        t.getStatus(),
        t.getDate());
  }

  /**
   * Valida los datos de entrada del request.
   *
   * @param request DTO de entrada
   */
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