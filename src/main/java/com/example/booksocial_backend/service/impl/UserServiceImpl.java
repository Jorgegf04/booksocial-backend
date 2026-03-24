package com.example.booksocial_backend.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.auth.RegisterRequest;
import com.example.booksocial_backend.DTO.user.CreateUserRequest;
import com.example.booksocial_backend.DTO.user.UpdateUserRequest;
import com.example.booksocial_backend.DTO.user.UserDTO;
import com.example.booksocial_backend.domain.user.Role;
import com.example.booksocial_backend.domain.user.User;

import com.example.booksocial_backend.repository.UserRepository;
import com.example.booksocial_backend.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  // ==============================
  // CREATE
  // ==============================

  @Override
  public UserDTO createUser(CreateUserRequest request) {

    validateCreateRequest(request);

    String username = request.username().trim();
    String email = request.email().trim().toLowerCase();

    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("El username ya existe");
    }

    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("El email ya está registrado");
    }

    User user = User.builder()
        .username(username)
        .email(email)
        .password(passwordEncoder.encode(request.password()))
        .name(request.name())
        .secondName(request.secondName())
        .registrationDate(LocalDate.now())
        .active(true)
        .role(request.role() != null ? request.role() : Role.REGISTERED)
        .build();

    return mapToDTO(userRepository.save(user));
  }

  // ==============================
  // READ
  // ==============================

  @Override
  @Transactional(readOnly = true)
  public UserDTO getUserById(Long id) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

    return mapToDTO(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDTO getUserByUsername(String username) {

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    return mapToDTO(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDTO> getAllUsers() {

    return userRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDTO> getActiveUsers() {

    return userRepository.findByActiveTrue()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDTO> getUsersByRole(Role role) {

    return userRepository.findByRole(role)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  // ==============================
  // UPDATE
  // ==============================

  @Override
  public UserDTO updateUser(Long id, UpdateUserRequest request) {

    validateUpdateRequest(request);

    User existing = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    String username = request.username().trim();
    String email = request.email().trim().toLowerCase();

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
    existing.setName(request.name());
    existing.setSecondName(request.secondName());

    return mapToDTO(userRepository.save(existing));
  }

  // ==============================
  // DELETE
  // ==============================

  @Override
  public void deleteUser(Long id) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    userRepository.delete(user);
  }

  // ==============================
  // STATUS
  // ==============================

  @Override
  public UserDTO setUserActive(Long id, Boolean active) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    user.setActive(active);

    return mapToDTO(userRepository.save(user));
  }

  // ==============================
  // MAPPER
  // ==============================

  private UserDTO mapToDTO(User user) {

    return new UserDTO(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getName(),
        user.getSecondName(),
        user.getRegistrationDate(),
        user.getActive(),
        user.getRole());
  }

  // ==============================
  // VALIDATION
  // ==============================

  private void validateCreateRequest(CreateUserRequest request) {

    if (request == null) {
      throw new IllegalArgumentException("Datos de usuario inválidos");
    }

    if (request.username() == null || request.username().trim().isEmpty()) {
      throw new IllegalArgumentException("El username es obligatorio");
    }

    if (request.email() == null || request.email().trim().isEmpty()) {
      throw new IllegalArgumentException("El email es obligatorio");
    }

    if (request.password() == null || request.password().length() < 6) {
      throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
    }
  }

  private void validateUpdateRequest(UpdateUserRequest request) {

    if (request == null) {
      throw new IllegalArgumentException("Datos de actualización inválidos");
    }

    if (request.username() == null || request.username().trim().isEmpty()) {
      throw new IllegalArgumentException("El username es obligatorio");
    }

    if (request.email() == null || request.email().trim().isEmpty()) {
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
    user.setRegistrationDate(LocalDate.now());

    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.REGISTERED);

    userRepository.save(user);
  }
}