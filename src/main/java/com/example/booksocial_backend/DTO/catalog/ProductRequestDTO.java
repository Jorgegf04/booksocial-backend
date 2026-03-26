package com.example.booksocial_backend.DTO.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para la creación y actualización de productos.
 *
 * Contiene los datos necesarios para registrar o modificar un producto
 * dentro del sistema de compra.
 *
 * Este DTO es utilizado en las peticiones de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    private Double price;
    private Integer stock;
    private Long editionId;
}