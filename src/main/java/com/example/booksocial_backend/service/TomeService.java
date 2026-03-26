package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.catalog.TomeRequestDTO;
import com.example.booksocial_backend.DTO.catalog.TomeResponseDTO;

/**
 * Servicio encargado de la gestión de tomos dentro del sistema.
 *
 * Los tomos forman parte de la estructura de obras tipo manga,
 * agrupando capítulos dentro de una edición.
 *
 * Este servicio se encarga de mantener la coherencia estructural,
 * evitando duplicados y permitiendo la recuperación ordenada.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
public interface TomeService {

  TomeResponseDTO createTome(TomeRequestDTO request);

  TomeResponseDTO getTomeById(Long id);

  List<TomeResponseDTO> getAllTomes();

  List<TomeResponseDTO> getTomesByEdition(Long editionId);

  List<TomeResponseDTO> getTomesByEditionOrdered(Long editionId);

  TomeResponseDTO updateTome(Long id, TomeRequestDTO request);

  void deleteTome(Long id);
}