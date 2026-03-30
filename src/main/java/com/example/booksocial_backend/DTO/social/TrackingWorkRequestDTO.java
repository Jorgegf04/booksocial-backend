package com.example.booksocial_backend.DTO.social;

import com.example.booksocial_backend.domain.social.TrackingWorkStatus;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO de entrada para registrar el seguimiento de una obra.
 *
 * Contiene los identificadores del usuario y la obra.
 *
 * @author Jorge
 * @since 2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingWorkRequestDTO {

  @NotNull
  private Long userId;
  @NotNull
  private Long workId;
  private TrackingWorkStatus status;
}