package com.example.booksocial_backend.domain.catalog;

/**
 * Demografía objetivo de una obra del catálogo.
 *
 * <p>Se aplica principalmente a manga y cómic para indicar
 * el público al que va dirigida la obra según la clasificación
 * editorial japonesa o occidental:</p>
 * <ul>
 *   <li>{@code SHONEN} — chicos jóvenes (12-18 años)</li>
 *   <li>{@code SEINEN} — hombres adultos</li>
 *   <li>{@code JOSEI} — mujeres adultas</li>
 *   <li>{@code KODOMO} — niños</li>
 *   <li>{@code SHOJO} — chicas jóvenes</li>
 * </ul>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
public enum Demographic {
  SHONEN,
  SEINEN,
  JOSEI,
  KODOMO,
  SHOJO
}