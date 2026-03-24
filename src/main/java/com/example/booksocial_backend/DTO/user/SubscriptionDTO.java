package com.example.booksocial_backend.DTO.user;

import java.time.LocalDate;

/**
 * DTO de salida para Subscription.
 */
public record SubscriptionDTO(

    Long id,

    LocalDate startDate,

    LocalDate endDate,

    Boolean activated,

    Long userId

) {
}