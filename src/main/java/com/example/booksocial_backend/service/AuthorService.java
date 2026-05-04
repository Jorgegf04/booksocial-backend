package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.catalog.AuthorRequestDTO;
import com.example.booksocial_backend.DTO.catalog.AuthorResponseDTO;

public interface AuthorService {

  AuthorResponseDTO createAuthor(AuthorRequestDTO request);

  AuthorResponseDTO getAuthorById(Long id);

  List<AuthorResponseDTO> getAllAuthors();

  List<AuthorResponseDTO> searchAuthorsByName(String name);

  List<AuthorResponseDTO> getAuthorsOrderedByName();

  List<AuthorResponseDTO> getAuthorsWithWorks();

  List<AuthorResponseDTO> getTopAuthors();

  AuthorResponseDTO updateAuthor(Long id, AuthorRequestDTO request);

  void deleteAuthor(Long id);

  void followAuthor(Long userId, Long authorId);

  void unfollowAuthor(Long userId, Long authorId);

  boolean isFollowing(Long userId, Long authorId);
}
