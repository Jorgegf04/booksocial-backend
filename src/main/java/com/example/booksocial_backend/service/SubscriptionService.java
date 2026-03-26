package com.example.booksocial_backend.service;

import com.example.booksocial_backend.DTO.user.SubscriptionRequestDTO;
import com.example.booksocial_backend.DTO.user.SubscriptionResponseDTO;

/**
 * Servicio de suscripciones.
 */
public interface SubscriptionService {

  SubscriptionResponseDTO activateSubscription(SubscriptionRequestDTO request);

  void cancelSubscription(Long userId);

  SubscriptionResponseDTO getSubscriptionByUserId(Long userId);

  boolean hasActiveSubscription(Long userId);
}