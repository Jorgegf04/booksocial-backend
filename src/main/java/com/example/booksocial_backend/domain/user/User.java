package com.example.booksocial_backend.domain.user;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jca.support.LocalConnectionFactoryBean;

import com.example.booksocial_backend.domain.social.TrackingWork;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidad que representa a un usuario registrado en la plataforma BookSocial.
 *
 * Un usuario puede realizar compras, publicar comentarios, reaccionar a
 * contenido,
 * seguir obras o seguir a otros usuarios. Además puede disponer de una
 * suscripción
 * activa que le proporciona beneficios adicionales dentro de la plataforma.
 *
 * El usuario invitado no se persiste en base de datos; su acceso se gestiona
 * mediante las reglas de autorización de Spring Security.
 * 
 * @author Jorge
 * @since 12/03/2026
 * @version 1.0
 */
@Entity
@Table(name = "APPUSER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

  /**
   * Identificador único del usuario generado automáticamente por la base de
   * datos.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Nombre de usuario utilizado para autenticarse en el sistema.
   */
  @NotBlank
  @Size(min = 3, max = 50)
  @Column(nullable = false, unique = true, length = 50)
  // @Pattern(regexp = "^[a-zA-Z0-9_]+$")
  private String username;

  /**
   * Contraseña del usuario almacenada de forma encriptada con BCrypt.
   * Nunca se devuelve en las respuestas de la API.
   */
  @NotBlank
  @Size(min = 6, max = 100)
  @Column(nullable = false)
  private String password;

  /**
   * Dirección de correo electrónico única del usuario.
   */
  @NotBlank
  @Email
  @Size(max = 100)
  @Column(nullable = false, unique = true, length = 100)
  private String email;

  /**
   * Nombre real del usuario.
   */
  @Size(max = 50)
  @Column(length = 50)
  private String name;

  /**
   * Apellido o segundo nombre del usuario.
   */

  @Size(max = 100)
  @Column(name = "second_name", length = 100)
  private String secondName;

  @Column(length = 500)
  private String img;

  /**
   * Fecha en la que el usuario se registró en la plataforma.
   * Se asigna automáticamente en el backend al crear el usuario.
   */
  @NotNull
  @PastOrPresent
  @Column(name = "registration_date", nullable = false)
  private LocalDate registrationDate;

  /**
   * Indica si la cuenta del usuario se encuentra activa.
   * Un usuario inactivo no puede iniciar sesión.
   */
  @NotNull
  @Column(nullable = false)
  private Boolean active = true;

  /**
   * Rol del usuario dentro del sistema.
   * Determina los permisos y funcionalidades disponibles.
   *
   * @see Role
   */

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Role role;

  /**
   * Suscripción activa del usuario.
   * Relación uno a uno opcional — no todos los usuarios tienen suscripción.
   */
  @ToString.Exclude
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Subscription subscription;

  /**
   * Lista de obras seguidas por el usuario.
   *
   * <p>
   * Representa la relación entre el usuario y las obras que sigue,
   * incluyendo el estado de seguimiento (pendiente, leyendo, completada, etc).
   * </p>
   *
   * <p>
   * Relación:
   * </p>
   * <ul>
   * <li>User → TrackingWork (1:N)</li>
   * </ul>
   *
   * <p>
   * Se utiliza carga perezosa (LAZY) para optimizar el rendimiento y evitar
   * cargar innecesariamente los seguimientos.
   * </p>
   */
  @JsonIgnore
  @ToString.Exclude
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<TrackingWork> trackingWorks;

  @OneToMany(mappedBy = "follower")
  private List<UserFollow> following;

  @OneToMany(mappedBy = "following")
  private List<UserFollow> followers;

}
