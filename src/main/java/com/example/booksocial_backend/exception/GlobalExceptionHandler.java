package com.example.booksocial_backend.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // =========================
  // USER
  // =========================

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<?> handleUserExists(UserAlreadyExistsException ex, HttpServletRequest request) {
    return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
  }

  // =========================
  // AUTHOR
  // =========================

  @ExceptionHandler(AuthorNotFoundException.class)
  public ResponseEntity<?> handleAuthorNotFound(AuthorNotFoundException ex, HttpServletRequest request) {
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  // =========================
  // WORK
  // =========================

  @ExceptionHandler(WorkNotFoundException.class)
  public ResponseEntity<?> handleWorkNotFound(WorkNotFoundException ex, HttpServletRequest request) {
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  // =========================
  // VALIDATION
  // =========================

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<?> handleValidation(ValidationException ex, HttpServletRequest request) {
    return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
  }

  // =========================
  // JSON ERROR
  // =========================

  @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
  public ResponseEntity<?> handleJsonError(Exception ex, HttpServletRequest request) {
    return buildResponse("JSON mal formado", HttpStatus.BAD_REQUEST, request);
  }

  // =========================
  // GENERIC
  // =========================

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGeneric(Exception ex, HttpServletRequest request) {

    ex.printStackTrace();

    return buildResponse("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  // =========================
  // BUILDER
  // =========================

  private ResponseEntity<?> buildResponse(String message, HttpStatus status, HttpServletRequest request) {

    return ResponseEntity.status(status).body(
        new ExceptionBody(
            LocalDateTime.now(),
            status.value(),
            message,
            request.getRequestURI()));
  }
}