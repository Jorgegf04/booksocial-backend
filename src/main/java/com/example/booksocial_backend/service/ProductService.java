package com.example.booksocial_backend.service;

import java.util.List;

import com.example.booksocial_backend.DTO.catalog.CreateProductRequest;
import com.example.booksocial_backend.DTO.catalog.ProductDTO;

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

  ProductDTO createProduct(CreateProductRequest request);

  ProductDTO getProductById(Long id);

  List<ProductDTO> getAllProducts();

  List<ProductDTO> getAvailableProducts();

  List<ProductDTO> getProductsByEdition(Long editionId);

  List<ProductDTO> getProductsByWork(Long workId);

  ProductDTO updateProduct(Long id, CreateProductRequest request);

  void decreaseStock(Long productId, int quantity);

  void deleteProduct(Long id);
}