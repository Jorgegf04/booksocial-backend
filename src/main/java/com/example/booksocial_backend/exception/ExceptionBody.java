package com.example.booksocial_backend.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO que estructura la respuesta de error devuelta por {@link GlobalExceptionHandler}.
 *
 * <p>Incluye el instante del error, el código HTTP, el mensaje descriptivo
 * y la ruta de la petición que lo originó, siguiendo el formato estándar
 * de errores de Spring Boot.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
@Getter
@AllArgsConstructor
public class ExceptionBody {

  private LocalDateTime timestamp;
  private int status;
  private String message;
  private String path;
}
// ¿Meter dentro de Global Exception como clase de paquete?