package com.example.booksocial_backend.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.catalog.Product;
import com.example.booksocial_backend.DTO.catalog.ProductRequestDTO;
import com.example.booksocial_backend.DTO.catalog.ProductResponseDTO;
import com.example.booksocial_backend.domain.catalog.Edition;
import com.example.booksocial_backend.repository.ProductRepository;
import com.example.booksocial_backend.service.ProductService;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio {@link ProductService}.
 *
 * Gestiona la lógica de negocio relacionada con los productos,
 * incluyendo control de stock, validación de precios y
 * disponibilidad para la compra.
 *
 * Este servicio es clave dentro del sistema de comercio electrónico.
 *
 * @author Jorge
 * @since 16/03/2026
 * @version 3.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ModelMapper modelMapper;

  @Override
  public ProductResponseDTO createProduct(ProductRequestDTO request) {

    Product product = modelMapper.map(request, Product.class);

    // Set manual de relación
    product.setEdition(Edition.builder().id(request.getEditionId()).build());

    validateProduct(product);

    Product saved = productRepository.save(product);

    return mapToDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public ProductResponseDTO getProductById(Long id) {

    Product product = getProductEntityById(id);

    return mapToDTO(product);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductResponseDTO> getAllProducts() {

    return productRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductResponseDTO> getAvailableProducts() {

    return productRepository.findByStockGreaterThan(0)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductResponseDTO> getProductsByEdition(Long editionId) {

    return productRepository.findByEditionId(editionId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductResponseDTO> getProductsByWork(Long workId) {

    return productRepository.findByEdition_Work_Id(workId)
        .stream()
        .map(this::mapToDTO)
        .toList();
  }

  @Override
  public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request) {

    Product existing = getProductEntityById(id);

    Product updated = modelMapper.map(request, Product.class);

    updated.setEdition(Edition.builder().id(request.getEditionId()).build());

    validateProduct(updated);

    existing.setPrice(updated.getPrice());
    existing.setStock(updated.getStock());
    existing.setEdition(updated.getEdition());

    Product saved = productRepository.save(existing);

    return mapToDTO(saved);
  }

  @Override
  public void decreaseStock(Long productId, int quantity) {

    Product product = getProductEntityById(productId);

    if (product.getStock() < quantity) {
      throw new IllegalArgumentException("Stock insuficiente");
    }

    product.setStock(product.getStock() - quantity);

    productRepository.save(product);
  }

  @Override
  public void deleteProduct(Long id) {

    Product product = getProductEntityById(id);

    productRepository.delete(product);
  }

  /**
   * Convierte Product → ProductDTO.
   */
  private ProductResponseDTO mapToDTO(Product product) {

    return new ProductResponseDTO(
        product.getId(),
        product.getPrice(),
        product.getStock(),
        product.getEdition().getId());
  }

  /**
   * Obtiene entidad Product o lanza excepción.
   */
  private Product getProductEntityById(Long id) {

    return productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
  }

  /**
   * Valida los datos de un producto.
   */
  private void validateProduct(Product product) {

    if (product == null) {
      throw new IllegalArgumentException("El producto no puede ser nulo");
    }

    if (product.getPrice() == null || product.getPrice() <= 0) {
      throw new IllegalArgumentException("El precio debe ser mayor que 0");
    }

    if (product.getStock() == null || product.getStock() < 0) {
      throw new IllegalArgumentException("El stock no puede ser negativo");
    }

    if (product.getEdition() == null || product.getEdition().getId() == null) {
      throw new IllegalArgumentException("El producto debe estar asociado a una edición válida");
    }
  }
}