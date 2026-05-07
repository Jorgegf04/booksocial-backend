package com.example.booksocial_backend.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.catalog.Product;
import com.example.booksocial_backend.DTO.catalog.ProductRequestDTO;
import com.example.booksocial_backend.DTO.catalog.ProductResponseDTO;
import com.example.booksocial_backend.domain.catalog.Edition;
import com.example.booksocial_backend.domain.catalog.Editorial;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.exception.EditionNotFoundException;
import com.example.booksocial_backend.exception.ProductNotFoundException;
import com.example.booksocial_backend.repository.EditionRepository;
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

  private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

  private final ProductRepository productRepository;
  private final EditionRepository editionRepository;
  private final ModelMapper modelMapper;

  @Override
  public ProductResponseDTO createProduct(ProductRequestDTO request) {

    log.info("[PRODUCT] [CREATE] [START] editionId={}", request.getEditionId());
    Product product = modelMapper.map(request, Product.class);

    Edition edition = editionRepository.findById(request.getEditionId())
        .orElseThrow(() -> new EditionNotFoundException("Edición no encontrada con id: " + request.getEditionId()));

    product.setEdition(edition);

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

    log.info("[PRODUCT] [UPDATE] [START] id={}", id);
    Product existing = getProductEntityById(id);

    Product updated = modelMapper.map(request, Product.class);

    Edition edition = editionRepository.findById(request.getEditionId())
        .orElseThrow(() -> new EditionNotFoundException("Edición no encontrada con id: " + request.getEditionId()));

    updated.setEdition(edition);

    validateProduct(updated);

    existing.setPrice(updated.getPrice());
    existing.setStock(updated.getStock());
    existing.setEdition(updated.getEdition());

    Product saved = productRepository.save(existing);

    return mapToDTO(saved);
  }

  @Override
  public void decreaseStock(Long id, int quantity) {

    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con id: " + id));

    if (quantity <= 0) {
      throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
    }

    if (product.getStock() < quantity) {
      throw new IllegalArgumentException("Stock insuficiente");
    }

    product.setStock(product.getStock() - quantity);
  }

  @Override
  public void deleteProduct(Long id) {

    log.info("[PRODUCT] [DELETE] [START] id={}", id);
    Product product = getProductEntityById(id);
    productRepository.delete(product);
    log.info("[PRODUCT] [DELETE] [SUCCESS] id={}", id);
  }

  /**
   * Convierte Product → ProductDTO.
   */
  private ProductResponseDTO mapToDTO(Product product) {

    Edition edition = product.getEdition();
    Work work = edition != null ? edition.getWork() : null;
    Editorial editorial = edition != null ? edition.getEditorial() : null;

    return new ProductResponseDTO(
        product.getId(),
        product.getPrice(),
        product.getStock(),

        edition != null ? edition.getId() : null,
        edition != null ? edition.getTitle() : null,
        edition != null ? edition.getIsbn() : null,
        edition != null ? edition.getTotalTomes() : null,

        work != null ? work.getId() : null,
        work != null ? work.getTitle() : null,

        editorial != null ? editorial.getName() : null);
  }

  /**
   * Obtiene entidad Product o lanza excepción.
   */
  private Product getProductEntityById(Long id) {

    return productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con id: " + id));
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