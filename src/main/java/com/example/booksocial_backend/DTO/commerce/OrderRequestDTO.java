package com.example.booksocial_backend.DTO.commerce;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para la creación de pedidos.
 *
 * Contiene la información necesaria para registrar
 * un pedido en el sistema, incluyendo el usuario
 * que realiza la compra y las líneas del pedido.
 *
 * Este DTO es utilizado en las peticiones de la API REST.
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    @NotNull
    private Long userId;
    @NotEmpty
    private List<OrderLineRequestDTO> orderLines;

}