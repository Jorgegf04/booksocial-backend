package com.example.booksocial_backend.domain.user;

public enum Role {
  /** Usuario registrado con acceso básico a la plataforma. */
  REGISTERED,
  /** Usuario registrado con suscripción mensual activa. */
  SUBSCRIBED,
  /** Administrador con privilegios completos de gestión. */
  ADMIN
}
