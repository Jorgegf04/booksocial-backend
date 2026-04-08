package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.booksocial_backend.DTO.user.CreateUserRequestDTO;
import com.example.booksocial_backend.DTO.user.UpdateUserRequestDTO;
import com.example.booksocial_backend.domain.user.Role;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.domain.user.UserFollow;
import com.example.booksocial_backend.repository.UserFollowRepository;
import com.example.booksocial_backend.repository.UserRepository;
import com.example.booksocial_backend.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserFollowRepository userFollowRepository;

  @InjectMocks
  private UserServiceImpl service;

  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    user.setUsername("jorge");
    user.setEmail("jorge@test.com");
    user.setName("Jorge");
    user.setSecondName("García");
    user.setRegistrationDate(LocalDate.now());
    user.setActive(true);
    user.setRole(Role.REGISTERED);
  }

  // =========================
  // CREATE
  // =========================

  @Test
  void shouldCreateUserSuccessfully() {

    CreateUserRequestDTO request = new CreateUserRequestDTO(
        "jorge", "password123", "jorge@test.com", "Jorge", "García", Role.REGISTERED);

    when(userRepository.existsByUsername("jorge")).thenReturn(false);
    when(userRepository.existsByEmail("jorge@test.com")).thenReturn(false);
    when(passwordEncoder.encode(any())).thenReturn("encoded");
    when(userRepository.save(any())).thenReturn(user);

    var result = service.createUser(request);

    assertNotNull(result);
    assertEquals("jorge", result.getUsername());
  }

  @Test
  void shouldThrowExceptionWhenUsernameAlreadyExists() {

    CreateUserRequestDTO request = new CreateUserRequestDTO(
        "jorge", "password123", "jorge@test.com", "Jorge", "García", Role.REGISTERED);

    when(userRepository.existsByUsername("jorge")).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> service.createUser(request));
  }

  @Test
  void shouldThrowExceptionWhenEmailAlreadyExists() {

    CreateUserRequestDTO request = new CreateUserRequestDTO(
        "jorge", "password123", "jorge@test.com", "Jorge", "García", Role.REGISTERED);

    when(userRepository.existsByUsername("jorge")).thenReturn(false);
    when(userRepository.existsByEmail("jorge@test.com")).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> service.createUser(request));
  }

  @Test
  void shouldThrowExceptionWhenPasswordTooShort() {

    CreateUserRequestDTO request = new CreateUserRequestDTO(
        "jorge", "123", "jorge@test.com", "Jorge", "García", Role.REGISTERED);

    assertThrows(IllegalArgumentException.class, () -> service.createUser(request));
  }

  // =========================
  // READ
  // =========================

  @Test
  void shouldGetUserById() {

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    var result = service.getUserById(1L);

    assertEquals(1L, result.getId());
    assertEquals("jorge", result.getUsername());
  }

  @Test
  void shouldThrowExceptionWhenUserNotFound() {

    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> service.getUserById(1L));
  }

  @Test
  void shouldGetAllUsers() {

    when(userRepository.findAll()).thenReturn(List.of(user));

    var result = service.getAllUsers();

    assertEquals(1, result.size());
  }

  @Test
  void shouldGetActiveUsers() {

    when(userRepository.findByActiveTrue()).thenReturn(List.of(user));

    var result = service.getActiveUsers();

    assertEquals(1, result.size());
  }

  @Test
  void shouldGetUsersByRole() {

    when(userRepository.findByRole(Role.REGISTERED)).thenReturn(List.of(user));

    var result = service.getUsersByRole(Role.REGISTERED);

    assertEquals(1, result.size());
    assertEquals(Role.REGISTERED, result.get(0).getRole());
  }

  // =========================
  // UPDATE
  // =========================

  @Test
  void shouldUpdateUserSuccessfully() {

    UpdateUserRequestDTO request = new UpdateUserRequestDTO(
        "jorge_updated", "jorge_updated@test.com", "Jorge", "García", null);

    User updated = new User();
    updated.setId(1L);
    updated.setUsername("jorge_updated");
    updated.setEmail("jorge_updated@test.com");
    updated.setName("Jorge");
    updated.setSecondName("García");
    updated.setRegistrationDate(LocalDate.now());
    updated.setActive(true);
    updated.setRole(Role.REGISTERED);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRepository.existsByUsername("jorge_updated")).thenReturn(false);
    when(userRepository.existsByEmail("jorge_updated@test.com")).thenReturn(false);
    when(userRepository.save(any())).thenReturn(updated);

    var result = service.updateUser(1L, request);

    assertEquals("jorge_updated", result.getUsername());
  }

  // =========================
  // DELETE
  // =========================

  @Test
  void shouldDeleteUser() {

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    service.deleteUser(1L);

    verify(userRepository).delete(user);
  }

  @Test
  void shouldThrowExceptionWhenDeletingNonExistingUser() {

    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> service.deleteUser(1L));
  }

  // =========================
  // STATUS
  // =========================

  @Test
  void shouldSetUserActive() {

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRepository.save(any())).thenReturn(user);

    var result = service.setUserActive(1L, false);

    verify(userRepository).save(user);
    assertNotNull(result);
  }

  // =========================
  // FOLLOW SYSTEM
  // =========================

  @Test
  void shouldFollowUserSuccessfully() {

    User target = new User();
    target.setId(2L);
    target.setUsername("pepe");

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRepository.findById(2L)).thenReturn(Optional.of(target));
    when(userFollowRepository.existsByFollowerAndFollowing(user, target)).thenReturn(false);

    service.followUser(1L, 2L);

    verify(userFollowRepository).save(any(UserFollow.class));
  }

  @Test
  void shouldThrowExceptionWhenFollowingSelf() {
    assertThrows(IllegalArgumentException.class, () -> service.followUser(1L, 1L));
  }

  @Test
  void shouldThrowExceptionWhenAlreadyFollowing() {

    User target = new User();
    target.setId(2L);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRepository.findById(2L)).thenReturn(Optional.of(target));
    when(userFollowRepository.existsByFollowerAndFollowing(user, target)).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> service.followUser(1L, 2L));
  }

  @Test
  void shouldUnfollowUserSuccessfully() {

    User target = new User();
    target.setId(2L);

    UserFollow follow = new UserFollow();
    follow.setFollower(user);
    follow.setFollowing(target);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRepository.findById(2L)).thenReturn(Optional.of(target));
    when(userFollowRepository.findByFollowerAndFollowing(user, target))
        .thenReturn(Optional.of(follow));

    service.unfollowUser(1L, 2L);

    verify(userFollowRepository).delete(follow);
  }
}
