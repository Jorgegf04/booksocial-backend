package com.example.booksocial_backend.domain.social;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.booksocial_backend.domain.user.User;


/**
 * Evento exclusivo para usuarios suscritos dentro de la plataforma.
 *
 * @author Jorge
 * @since 15/03/2026
 * @version 1.0
 */

@Entity
@Table(name = "EVENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

  /**
   * Identificador del evento.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Nombre del evento.
   */
  @NotBlank
  @Size(max = 150)
  @Column(nullable = false, length = 150)
  private String title;

  /**
   * Descripcion del evento
   */
  @Size(max = 1000)
  @Column(length = 1000)
  private String description;

  /**
   * Imagen representativa del evento.
   */
  @Size(max = 500)
  @Column(length = 500)
  private String img;

  /**
   * Fecha del evento.
   */
  @NotNull
  @Future
  private LocalDateTime date;

  /**
   * Relacion de eventos con usuarios
   */
  @Builder.Default
  @ToString.Exclude
  @ManyToMany
  @JoinTable(name = "USER_EVENT", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> users = new ArrayList<>();
}