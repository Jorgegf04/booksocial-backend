package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.catalog.CreateTomeRequest;
import com.example.booksocial_backend.DTO.catalog.TomeDTO;

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

  TomeDTO createTome(CreateTomeRequest request);

  TomeDTO getTomeById(Long id);

  List<TomeDTO> getAllTomes();

  List<TomeDTO> getTomesByEdition(Long editionId);

  List<TomeDTO> getTomesByEditionOrdered(Long editionId);

  TomeDTO updateTome(Long id, CreateTomeRequest request);

  void deleteTome(Long id);
}