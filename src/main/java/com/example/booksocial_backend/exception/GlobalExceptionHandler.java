package com.example.booksocial_backend.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Gestión global de excepciones REST.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<?> handleUserNotFound(
      UserNotFoundException ex,
      HttpServletRequest request) {

    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<?> handleUserExists(
      UserAlreadyExistsException ex,
      HttpServletRequest request) {

    return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleValidation(
      IllegalArgumentException ex,
      HttpServletRequest request) {

    return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGeneric(
      Exception ex,
      HttpServletRequest request) {

    return buildResponse("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  private ResponseEntity<?> buildResponse(
      String message,
      HttpStatus status,
      HttpServletRequest request) {

    return ResponseEntity.status(status).body(
        new ExceptionBody(
            LocalDateTime.now(),
            status.value(),
            message,
            request.getRequestURI()));
  }
}