package com.example.booksocial_backend.DTO.social;

import java.time.LocalDateTime;

import com.example.booksocial_backend.domain.social.TrackingWorkStatus;

import lombok.*;

/**
 * DTO de salida para el seguimiento de obras.
 *
 * Representa la información del seguimiento realizado por un usuario.
 *
 * @author Jorge
 * @since 2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingWorkResponseDTO {

  private Long id;

  private Long userId;
  private String username;

  private Long workId;
  private String workTitle;

  private TrackingWorkStatus status;
  private String statusLabel;

  private LocalDateTime date;

  private Boolean completed;

}