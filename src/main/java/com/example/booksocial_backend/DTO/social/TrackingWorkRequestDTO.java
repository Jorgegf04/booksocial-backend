package com.example.booksocial_backend.DTO.social;

import com.example.booksocial_backend.domain.social.TrackingWorkStatus;

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

  private Long userId;
  private Long workId;
  private TrackingWorkStatus status;
}