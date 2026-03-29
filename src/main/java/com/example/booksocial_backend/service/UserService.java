package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.auth.RegisterRequest;
import com.example.booksocial_backend.DTO.user.CreateUserRequestDTO;
import com.example.booksocial_backend.DTO.user.UpdateUserRequestDTO;
import com.example.booksocial_backend.DTO.user.UserResponseDTO;
import com.example.booksocial_backend.domain.user.Role;

public interface UserService {

  UserResponseDTO createUser(CreateUserRequestDTO request);

  UserResponseDTO getUserById(Long id);

  UserResponseDTO getUserByUsername(String username);

  List<UserResponseDTO> getAllUsers();

  List<UserResponseDTO> getActiveUsers();

  List<UserResponseDTO> getUsersByRole(Role role);

  UserResponseDTO updateUser(Long id, UpdateUserRequestDTO request);

  void deleteUser(Long id);

  UserResponseDTO setUserActive(Long id, Boolean active);

  void registerUser(RegisterRequest request);

  void followUser(Long userId, Long targetId);

  void unfollowUser(Long userId, Long targetId);

  List<UserResponseDTO> getFollowers(Long userId);

  List<UserResponseDTO> getFollowing(Long userId);
}