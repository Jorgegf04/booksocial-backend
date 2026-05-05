package com.example.booksocial_backend.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.auth.RegisterRequest;
import com.example.booksocial_backend.DTO.user.CreateUserRequestDTO;
import com.example.booksocial_backend.DTO.user.UpdateUserRequestDTO;
import com.example.booksocial_backend.DTO.user.UserResponseDTO;
import com.example.booksocial_backend.domain.user.Role;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.domain.user.UserFollow;
import com.example.booksocial_backend.repository.UserFollowRepository;
import com.example.booksocial_backend.repository.UserRepository;
import com.example.booksocial_backend.exception.UserNotFoundException;
import com.example.booksocial_backend.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link UserService}.
 *
 * <p>Gestiona el ciclo de vida completo de los usuarios de BookSocial:
 * registro, consulta, actualización, baja lógica y el sistema de
 * seguimiento social (follow/unfollow).</p>
 *
 * <h3>Sistema de seguimiento entre usuarios</h3>
 * <p>La relación de seguimiento se modela con la entidad {@code UserFollow},
 * que almacena el par (follower, following) junto a la fecha del seguimiento.
 * Una restricción de unicidad en base de datos impide duplicados.
 * No se permite que un usuario se siga a sí mismo.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserFollowRepository userFollowRepository;

  // ==============================
  // CREATE
  // ==============================

  @Override
  public UserResponseDTO createUser(CreateUserRequestDTO request) {

    validateCreateRequest(request);

    String username = request.getUsername().trim();
    String email = request.getEmail().trim().toLowerCase();

    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("El username ya existe");
    }

    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("El email ya está registrado");
    }

    User user = User.builder()
        .username(username)
        .email(email)
        .password(passwordEncoder.encode(request.getPassword()))
        .name(request.getName())
        .secondName(request.getSecondName())
        .registrationDate(LocalDate.now())
        .active(true)
        .role(request.getRole() != null ? request.getRole() : Role.REGISTERED)
        .build();

    return mapToDTO(userRepository.save(user));
  }

  // ==============================
  // READ
  // ==============================

  @Override
  @Transactional(readOnly = true)
  public UserResponseDTO getUserById(Long id) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

    return mapToDTO(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponseDTO getUserByUsername(String username) {

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

    return mapToDTO(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponseDTO> getAllUsers() {

    return userRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponseDTO> getActiveUsers() {

    return userRepository.findByActiveTrue()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponseDTO> getUsersByRole(Role role) {

    return userRepository.findByRole(role)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  // ==============================
  // UPDATE
  // ==============================

  @Override
  public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO request) {

    validateUpdateRequest(request);

    User existing = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

    String username = request.getUsername().trim();
    String email = request.getEmail().trim().toLowerCase();

    if (!existing.getUsername().equalsIgnoreCase(username)
        && userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("El username ya existe");
    }

    if (!existing.getEmail().equalsIgnoreCase(email)
        && userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("El email ya está registrado");
    }

    existing.setUsername(username);
    existing.setEmail(email);
    existing.setName(request.getName());
    existing.setSecondName(request.getSecondName());
    existing.setImg(request.getImg());

    return mapToDTO(userRepository.save(existing));
  }

  // ==============================
  // DELETE
  // ==============================

  @Override
  public void deleteUser(Long id) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

    userRepository.delete(user);
  }

  // ==============================
  // STATUS
  // ==============================

  @Override
  public UserResponseDTO setUserActive(Long id, Boolean active) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

    user.setActive(active);

    return mapToDTO(userRepository.save(user));
  }

  // ==============================
  // MAPPER
  // ==============================

  private UserResponseDTO mapToDTO(User user) {

    return new UserResponseDTO(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getName(),
        user.getSecondName(),
        user.getImg(),
        user.getRegistrationDate(),
        user.getActive(),
        user.getRole());
  }

  // ==============================
  // VALIDATION
  // ==============================

  private void validateCreateRequest(CreateUserRequestDTO request) {

    if (request == null) {
      throw new IllegalArgumentException("Datos de usuario inválidos");
    }

    if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
      throw new IllegalArgumentException("El username es obligatorio");
    }

    if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
      throw new IllegalArgumentException("El email es obligatorio");
    }

    if (request.getPassword() == null || request.getPassword().length() < 6) {
      throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
    }
  }

  private void validateUpdateRequest(UpdateUserRequestDTO request) {

    if (request == null) {
      throw new IllegalArgumentException("Datos de actualización inválidos");
    }

    if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
      throw new IllegalArgumentException("El username es obligatorio");
    }

    if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
      throw new IllegalArgumentException("El email es obligatorio");
    }
  }

  @Override
  public void registerUser(RegisterRequest request) {

    if (userRepository.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("El username ya existe");
    }

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("El email ya existe");
    }

    User user = new User();

    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setName(request.getName());
    user.setSecondName(request.getSecondName());
    user.setImg(request.getImg());
    user.setRegistrationDate(LocalDate.now());
    user.setActive(true);
    user.setRole(Role.REGISTERED);

    userRepository.save(user);
  }

  @Override
  public void followUser(Long userId, Long targetId) {

    if (userId.equals(targetId)) {
      throw new IllegalArgumentException("No puedes seguirte a ti mismo");
    }

    User follower = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

    User following = userRepository.findById(targetId)
        .orElseThrow(() -> new UserNotFoundException("Usuario objetivo no encontrado"));

    if (userFollowRepository.existsByFollowerAndFollowing(follower, following)) {
      throw new IllegalArgumentException("Ya sigues a este usuario");
    }

    UserFollow follow = UserFollow.builder()
        .follower(follower)
        .following(following)
        .followDate(java.time.LocalDateTime.now())
        .build();

    userFollowRepository.save(follow);
  }

  @Override
  public void unfollowUser(Long userId, Long targetId) {

    User follower = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

    User following = userRepository.findById(targetId)
        .orElseThrow(() -> new UserNotFoundException("Usuario objetivo no encontrado"));

    UserFollow relation = userFollowRepository
        .findByFollowerAndFollowing(follower, following)
        .orElseThrow(() -> new IllegalArgumentException("No sigues a este usuario"));

    userFollowRepository.delete(relation);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponseDTO> getFollowers(Long userId) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

    return userFollowRepository.findByFollowing(user)
        .stream()
        .map(f -> mapToDTO(f.getFollower()))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponseDTO> getFollowing(Long userId) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

    return userFollowRepository.findByFollower(user)
        .stream()
        .map(f -> mapToDTO(f.getFollowing()))
        .toList();
  }
}