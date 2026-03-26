package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.catalog.ProductRequestDTO;
import com.example.booksocial_backend.DTO.catalog.ProductResponseDTO;

/**
 * Servicio encargado de la gestión de productos dentro del sistema de compra.
 *
 * Los productos representan unidades comercializables asociadas a una edición,
 * incluyendo información de precio y stock disponible.
 *
 * Este servicio se encarga de gestionar la disponibilidad de productos,
 * validar operaciones de compra y mantener la integridad del stock.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
public interface ProductService {

  ProductResponseDTO createProduct(ProductRequestDTO request);

  ProductResponseDTO getProductById(Long id);

  List<ProductResponseDTO> getAllProducts();

  List<ProductResponseDTO> getAvailableProducts();

  List<ProductResponseDTO> getProductsByEdition(Long editionId);

  List<ProductResponseDTO> getProductsByWork(Long workId);

  ProductResponseDTO updateProduct(Long id, ProductRequestDTO request);

  void decreaseStock(Long productId, int quantity);

  void deleteProduct(Long id);
}