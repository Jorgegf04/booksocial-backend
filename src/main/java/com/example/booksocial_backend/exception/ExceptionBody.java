package com.example.booksocial_backend.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * DTO que estructura la respuesta de error devuelta por {@link GlobalExceptionHandler}.
 *
 * <p>Incluye timestamp, código HTTP, mensaje, ruta de la petición y,
 * opcionalmente, el detalle de errores por campo para errores de validación.</p>
 */
@Getter
public class ExceptionBody {

  private final LocalDateTime timestamp;
  private final int status;
  private final String message;
  private final String path;
  private final List<Map<String, String>> fieldErrors;

  public ExceptionBody(LocalDateTime timestamp, int status, String message, String path) {
    this.timestamp = timestamp;
    this.status = status;
    this.message = message;
    this.path = path;
    this.fieldErrors = null;
  }

  public ExceptionBody(LocalDateTime timestamp, int status, String message, String path,
      List<Map<String, String>> fieldErrors) {
    this.timestamp = timestamp;
    this.status = status;
    this.message = message;
    this.path = path;
    this.fieldErrors = fieldErrors;
  }
}
