package com.example.booksocial_backend.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

/**
 * Manejador global de excepciones para la API REST de BookSocial.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  // =========================
  // USER
  // =========================

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
    log.warn("[USER] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ExceptionBody> handleUserExists(UserAlreadyExistsException ex, HttpServletRequest request) {
    log.warn("[USER] [CREATE] [CONFLICT] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
  }

  // =========================
  // AUTHOR
  // =========================

  @ExceptionHandler(AuthorNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleAuthorNotFound(AuthorNotFoundException ex, HttpServletRequest request) {
    log.warn("[AUTHOR] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(AuthorAlreadyExistsException.class)
  public ResponseEntity<ExceptionBody> handleAuthorExists(AuthorAlreadyExistsException ex, HttpServletRequest request) {
    log.warn("[AUTHOR] [CREATE] [CONFLICT] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
  }

  // =========================
  // WORK
  // =========================

  @ExceptionHandler(WorkNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleWorkNotFound(WorkNotFoundException ex, HttpServletRequest request) {
    log.warn("[WORK] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  // =========================
  // EDITION / EDITORIAL / VOLUME / PRODUCT
  // =========================

  @ExceptionHandler(EditionNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleEditionNotFound(EditionNotFoundException ex, HttpServletRequest request) {
    log.warn("[EDITION] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(EditorialNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleEditorialNotFound(EditorialNotFoundException ex, HttpServletRequest request) {
    log.warn("[EDITORIAL] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(VolumeNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleVolumeNotFound(VolumeNotFoundException ex, HttpServletRequest request) {
    log.warn("[VOLUME] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleProductNotFound(ProductNotFoundException ex, HttpServletRequest request) {
    log.warn("[PRODUCT] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  // =========================
  // ORDER / ORDER LINE
  // =========================

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleOrderNotFound(OrderNotFoundException ex, HttpServletRequest request) {
    log.warn("[ORDER] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(OrderLineNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleOrderLineNotFound(OrderLineNotFoundException ex, HttpServletRequest request) {
    log.warn("[ORDER_LINE] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  // =========================
  // COMMENT / REACTION
  // =========================

  @ExceptionHandler(CommentNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleCommentNotFound(CommentNotFoundException ex, HttpServletRequest request) {
    log.warn("[COMMENT] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(ReactionNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleReactionNotFound(ReactionNotFoundException ex, HttpServletRequest request) {
    log.warn("[REACTION] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  // =========================
  // SUBSCRIPTION / EVENT / TRACKING
  // =========================

  @ExceptionHandler(SubscriptionNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleSubscriptionNotFound(SubscriptionNotFoundException ex, HttpServletRequest request) {
    log.warn("[SUBSCRIPTION] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(SubscriptionAlreadyActiveException.class)
  public ResponseEntity<ExceptionBody> handleSubscriptionAlreadyActive(SubscriptionAlreadyActiveException ex, HttpServletRequest request) {
    log.warn("[SUBSCRIPTION] [CREATE] [CONFLICT] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleEventNotFound(EventNotFoundException ex, HttpServletRequest request) {
    log.warn("[EVENT] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(TrackingWorkNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleTrackingWorkNotFound(TrackingWorkNotFoundException ex, HttpServletRequest request) {
    log.warn("[TRACKING_WORK] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(TrackingWorkAlreadyExistsException.class)
  public ResponseEntity<ExceptionBody> handleTrackingWorkExists(TrackingWorkAlreadyExistsException ex, HttpServletRequest request) {
    log.warn("[TRACKING_WORK] [CREATE] [CONFLICT] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler(TrackingOrderNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleTrackingOrderNotFound(TrackingOrderNotFoundException ex, HttpServletRequest request) {
    log.warn("[TRACKING_ORDER] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  // =========================
  // EDITORIAL (EXTENDED)
  // =========================

  @ExceptionHandler(EditorialAlreadyExistsException.class)
  public ResponseEntity<ExceptionBody> handleEditorialExists(EditorialAlreadyExistsException ex, HttpServletRequest request) {
    log.warn("[EDITORIAL] [CREATE] [CONFLICT] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler(EditorialHasEditionsException.class)
  public ResponseEntity<ExceptionBody> handleEditorialHasEditions(EditorialHasEditionsException ex, HttpServletRequest request) {
    log.warn("[EDITORIAL] [DELETE] [CONFLICT] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
  }

  // =========================
  // EDITION (EXTENDED)
  // =========================

  @ExceptionHandler(EditionAlreadyExistsException.class)
  public ResponseEntity<ExceptionBody> handleEditionExists(EditionAlreadyExistsException ex, HttpServletRequest request) {
    log.warn("[EDITION] [CREATE] [CONFLICT] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler(EditionHasOrderLinesException.class)
  public ResponseEntity<ExceptionBody> handleEditionHasOrderLines(EditionHasOrderLinesException ex, HttpServletRequest request) {
    log.warn("[EDITION] [DELETE] [CONFLICT] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
  }

  // =========================
  // TOME (EXTENDED)
  // =========================

  @ExceptionHandler(TomeAlreadyExistsException.class)
  public ResponseEntity<ExceptionBody> handleTomeExists(TomeAlreadyExistsException ex, HttpServletRequest request) {
    log.warn("[TOME] [CREATE] [CONFLICT] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
  }

  // =========================
  // CHAPTER / TOME
  // =========================

  @ExceptionHandler(ChapterNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleChapterNotFound(ChapterNotFoundException ex, HttpServletRequest request) {
    log.warn("[CHAPTER] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(ChapterAlreadyExistsException.class)
  public ResponseEntity<ExceptionBody> handleChapterExists(ChapterAlreadyExistsException ex, HttpServletRequest request) {
    log.warn("[CHAPTER] [CREATE] [CONFLICT] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler(TomeNotFoundException.class)
  public ResponseEntity<ExceptionBody> handleTomeNotFound(TomeNotFoundException ex, HttpServletRequest request) {
    log.warn("[TOME] [READ] [NOT_FOUND] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  // =========================
  // VALIDATION
  // =========================

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ExceptionBody> handleValidation(ValidationException ex, HttpServletRequest request) {
    log.warn("[VALIDATION] [ERROR] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ExceptionBody> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
        .map(fe -> Map.of("field", fe.getField(), "message",
            fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Valor inválido"))
        .toList();
    log.warn("[VALIDATION] [BEAN] [ERROR] {} campo(s) inválido(s): {}", fieldErrors.size(), fieldErrors);
    return buildResponseWithFields("Errores de validación en la petición", HttpStatus.BAD_REQUEST, request, fieldErrors);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ExceptionBody> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
    String message = ex.getConstraintViolations().stream()
        .findFirst()
        .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
        .orElse("Restricción de validación violada");
    log.warn("[VALIDATION] [CONSTRAINT] [ERROR] {}", message);
    return buildResponse(message, HttpStatus.BAD_REQUEST, request);
  }

  // =========================
  // JSON ERROR
  // =========================

  @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
  public ResponseEntity<ExceptionBody> handleJsonError(Exception ex, HttpServletRequest request) {
    log.warn("[REQUEST] [PARSE] [ERROR] JSON mal formado: {}", ex.getMessage());
    return buildResponse("El cuerpo de la petición tiene formato inválido", HttpStatus.BAD_REQUEST, request);
  }

  // =========================
  // INTEGRIDAD DE DATOS (FK, UNIQUE, NOT NULL en BD)
  // DEBE ir ANTES que DataAccessException porque es subclase
  // =========================

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ExceptionBody> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
    Throwable root = ex.getRootCause();
    String rootMsg = (root != null) ? root.getMessage() : ex.getMessage();
    log.warn("[DB] [INTEGRITY] [CONFLICT] {}", rootMsg);

    String userMessage;
    if (rootMsg != null && rootMsg.toLowerCase().contains("duplicate")) {
      userMessage = "Ya existe un registro con esos datos (clave duplicada)";
    } else if (rootMsg != null && rootMsg.toLowerCase().contains("foreign key")) {
      userMessage = "No se puede eliminar el registro porque tiene datos relacionados (pedidos, comentarios u otras referencias)";
    } else if (rootMsg != null && rootMsg.toLowerCase().contains("cannot be null")) {
      userMessage = "Faltan campos obligatorios en la base de datos";
    } else {
      userMessage = "Operación no permitida por restricción de integridad de datos";
    }

    return buildResponse(userMessage, HttpStatus.CONFLICT, request);
  }

  // =========================
  // BASE DE DATOS
  // =========================

  @ExceptionHandler(CannotCreateTransactionException.class)
  public ResponseEntity<ExceptionBody> handleCannotCreateTransaction(CannotCreateTransactionException ex, HttpServletRequest request) {
    log.error("[DB] [CONNECTION] [ERROR] No se puede conectar con la base de datos: {}", ex.getMessage());
    return buildResponse("No se puede conectar con la base de datos. Verifique que MySQL está activo.",
        HttpStatus.SERVICE_UNAVAILABLE, request);
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ExceptionBody> handleDataAccess(DataAccessException ex, HttpServletRequest request) {
    log.error("[DB] [ACCESS] [ERROR] Error de acceso a datos en {}: {}", request.getRequestURI(), ex.getMessage());
    return buildResponse("Error de acceso a datos. Por favor inténtelo más tarde.",
        HttpStatus.SERVICE_UNAVAILABLE, request);
  }

  // =========================
  // STOCK / AUTORIZACIÓN
  // =========================

  @ExceptionHandler(InsufficientStockException.class)
  public ResponseEntity<ExceptionBody> handleInsufficientStock(InsufficientStockException ex, HttpServletRequest request) {
    log.warn("[ORDER] [STOCK] [UNPROCESSABLE] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY, request);
  }

  @ExceptionHandler(UnauthorizedActionException.class)
  public ResponseEntity<ExceptionBody> handleUnauthorizedAction(UnauthorizedActionException ex, HttpServletRequest request) {
    log.warn("[SECURITY] [UNAUTHORIZED] [FORBIDDEN] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN, request);
  }

  // =========================
  // ILLEGAL ARGUMENT
  // =========================

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ExceptionBody> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
    log.warn("[REQUEST] [VALIDATION] [ERROR] {}", ex.getMessage());
    return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
  }

  // =========================
  // GENERIC
  // =========================

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionBody> handleGeneric(Exception ex, HttpServletRequest request) {
    log.error("[SYSTEM] [UNHANDLED] [ERROR] {} en {}: {}", ex.getClass().getSimpleName(), request.getRequestURI(), ex.getMessage(), ex);
    return buildResponse("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  // =========================
  // BUILDERS
  // =========================

  private ResponseEntity<ExceptionBody> buildResponse(String message, HttpStatus status, HttpServletRequest request) {
    return ResponseEntity.status(status.value()).body(
        new ExceptionBody(
            LocalDateTime.now(),
            status.value(),
            message,
            request.getRequestURI()));
  }

  private ResponseEntity<ExceptionBody> buildResponseWithFields(String message, HttpStatus status,
      HttpServletRequest request, List<Map<String, String>> fieldErrors) {
    return ResponseEntity.status(status.value()).body(
        new ExceptionBody(
            LocalDateTime.now(),
            status.value(),
            message,
            request.getRequestURI(),
            fieldErrors));
  }
}
