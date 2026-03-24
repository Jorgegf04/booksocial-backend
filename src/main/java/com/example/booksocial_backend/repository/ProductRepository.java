package com.example.booksocial_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.catalog.Product;

/**
 * Repositorio de acceso a datos para la entidad {@link Product}.
 *
 * Gestiono la persistencia de los productos dentro del sistema BookSocial,
 * proporcionando operaciones CRUD básicas y métodos de consulta relacionados
 * con la disponibilidad y gestión de stock.
 *
 * Este repositorio es fundamental dentro del sistema de compra, ya que permite
 * identificar qué productos están disponibles para la venta en función de su
 * stock.
 *
 * @author Jorge
 * @since 22/03/2026
 * @version 1.1
 */

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  /**
   * Obtiene los productos con stock superior al valor indicado.
   *
   * @param stock cantidad mínima de stock
   * @return lista de productos disponibles
   */
  List<Product> findByStockGreaterThan(Integer stock);

  /**
   * Buscar por edición
   *
   * @param editionId id de la edicion
   * @return lista edicion
   */
  List<Product> findByEditionId(Long editionId);

  /**
   * Busca productos disponibles por obra
   *
   * @param workId id de la obra
   * @return lista edicion de productos segun una obra
   */
  List<Product> findByEdition_Work_Id(Long workId);

}