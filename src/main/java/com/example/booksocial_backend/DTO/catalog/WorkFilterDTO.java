package com.example.booksocial_backend.DTO.catalog;

import java.time.LocalDate;

import com.example.booksocial_backend.domain.catalog.Demographic;
import com.example.booksocial_backend.domain.catalog.Genre;
import com.example.booksocial_backend.domain.catalog.WorkType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de filtrado para la búsqueda avanzada de obras en el catálogo.
 *
 * <p>Todos los campos son opcionales; si se omite un campo, ese criterio
 * no se aplica al filtrado. El servicio {@code WorkServiceImpl} construye
 * la consulta combinando únicamente los campos informados.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkFilterDTO {

  private String title;
  private Genre genre;
  private WorkType type;
  private Demographic demographic;
  private Double minRating;
  private LocalDate publishedAfter;
  private LocalDate publishedBefore;
  private Long authorId;

}