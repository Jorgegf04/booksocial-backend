package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.auth.RegisterRequest;
import com.example.booksocial_backend.DTO.user.CreateUserRequest;
import com.example.booksocial_backend.DTO.user.UpdateUserRequest;
import com.example.booksocial_backend.DTO.user.UserDTO;
import com.example.booksocial_backend.domain.user.Role;

public interface UserService {

  UserDTO createUser(CreateUserRequest request);

  UserDTO getUserById(Long id);

  UserDTO getUserByUsername(String username);

  List<UserDTO> getAllUsers();

  List<UserDTO> getActiveUsers();

  List<UserDTO> getUsersByRole(Role role);

  UserDTO updateUser(Long id, UpdateUserRequest request);

  void deleteUser(Long id);

  UserDTO setUserActive(Long id, Boolean active);

  void registerUser(RegisterRequest request);
}