package com.example.booksocial_backend.DTO.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la entidad Product.
 *
 * Representa un producto comercializable dentro del sistema,
 * incluyendo información de precio, stock y la edición asociada
 * mediante su identificador.
 *
 * Evita exponer directamente la entidad Edition.
 *
 * Este DTO es utilizado en las respuestas de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private Long id;
    private Double price;
    private Integer stock;
    private Long editionId;
    private String editionIsbn;
    private String workTitle;
}