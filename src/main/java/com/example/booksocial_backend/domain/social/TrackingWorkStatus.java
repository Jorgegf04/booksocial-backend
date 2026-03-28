package com.example.booksocial_backend.domain.social;

/**
 * Enum que representa el estado de seguimiento de una obra.
 *
 * Permite indicar el progreso del usuario respecto a una obra.
 *
 * @author Jorge
 * @since 2026
 */
public enum TrackingWorkStatus {

  /**
   * El usuario ha añadido la obra pero aún no la ha empezado.
   */
  PENDING,

  /**
   * El usuario está leyendo la obra actualmente.
   */
  READING,

  /**
   * El usuario ha completado la obra.
   */
  COMPLETED,

  /**
   * El usuario ha abandonado la obra.
   */
  DROPPED
}
