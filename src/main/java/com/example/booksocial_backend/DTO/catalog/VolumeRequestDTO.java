package com.example.booksocial_backend.DTO.catalog;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para la creación y actualización de volúmenes.
 *
 * Contiene los datos necesarios para registrar o modificar un volumen
 * dentro de una edición concreta.
 *
 * Este DTO es utilizado en las peticiones de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolumeRequestDTO {

    @NotNull(message = "El número de volumen es obligatorio")
    @Positive
    private Integer volumeNumber;

    @Size(max = 200)
    private String title;

    @NotNull(message = "El volumen debe estar asociado a una edición")
    private Long editionId;
}