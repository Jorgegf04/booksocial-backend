package com.example.booksocial_backend.DTO.commerce;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para la creación de pedidos.
 *
 * Soporta dos modos:
 * - Usuario registrado: proporciona userId.
 * - Invitado: proporciona guestEmail (sin userId).
 *
 * @author Jorge
 * @since 26/03/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    /** ID del usuario registrado. Null si el comprador es invitado. */
    private Long userId;

    /** Email del comprador invitado. Requerido si userId es null. */
    @Email
    private String guestEmail;

    @NotEmpty
    private List<OrderLineRequestDTO> orderLines;

}
