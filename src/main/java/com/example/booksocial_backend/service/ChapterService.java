package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.catalog.ChapterResponseDTO;
import com.example.booksocial_backend.DTO.catalog.ChapterRequestDTO;
import com.example.booksocial_backend.domain.catalog.Chapter;

/**
 * Servicio encargado de la gestión de capítulos dentro del sistema BookSocial.
 *
 * Este servicio forma parte del módulo de catálogo, concretamente de la
 * estructura de obras tipo manga, donde los capítulos se organizan en tomos.
 *
 * Proporciona operaciones de creación, consulta y eliminación de capítulos,
 * así como métodos específicos para recuperar capítulos asociados a un tomo.
 *
 * Se encarga de validar la coherencia de los datos, evitando duplicados
 * y garantizando la integridad de la estructura jerárquica Tome → Chapter.
 *
 * @author Jorge
 * @since 12/03/2026
 * @version 2.0
 */
public interface ChapterService {

  /**
   * Crea un nuevo capítulo dentro de un tomo.
   *
   * @param chapter datos del capítulo
   * @return capítulo persistido
   * @throws IllegalArgumentException si los datos son inválidos o duplicados
   */
  ChapterResponseDTO createChapter(ChapterRequestDTO request);

  /**
   * Obtiene un capítulo por su identificador.
   *
   * @param id identificador del capítulo
   * @return capítulo encontrado
   */
  ChapterResponseDTO getChapterById(Long id);

  /**
   * Obtiene todos los capítulos del sistema.
   *
   * @return lista de capítulos
   */
  List<ChapterResponseDTO> getAllChapters();

  /**
   * Obtiene todos los capítulos pertenecientes a un tomo.
   *
   * @param tomeId identificador del tomo
   * @return lista de capítulos
   */
  List<ChapterResponseDTO> getChaptersByTome(Long tomeId);

  /**
   * Elimina un capítulo del sistema.
   *
   * @param id identificador del capítulo
   */

  void deleteChapter(Long id);
}