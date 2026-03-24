package com.example.booksocial_backend.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionBody {

  private LocalDateTime timestamp;
  private int status;
  private String message;
  private String path;
}