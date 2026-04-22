package com.example.booksocial_backend.domain.catalog;

/**
 * Tipo de obra dentro del catálogo BookSocial.
 *
 * <p>Determina la estructura interna de la obra:</p>
 * <ul>
 *   <li>{@code BOOK} — libro tradicional, sin subdivisión en tomos ni volúmenes.</li>
 *   <li>{@code MANGA} — organizado en {@code Tome} → {@code Chapter}.</li>
 *   <li>{@code COMIC} — organizado en {@code Volume}.</li>
 * </ul>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
public enum WorkType {
  BOOK,
  MANGA,
  COMIC
}