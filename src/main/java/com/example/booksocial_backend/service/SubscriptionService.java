package com.example.booksocial_backend.service;

import com.example.booksocial_backend.DTO.user.ActivateSubscriptionRequest;
import com.example.booksocial_backend.DTO.user.SubscriptionDTO;

/**
 * Servicio de suscripciones.
 */
public interface SubscriptionService {

  SubscriptionDTO activateSubscription(ActivateSubscriptionRequest request);

  void cancelSubscription(Long userId);

  SubscriptionDTO getSubscriptionByUserId(Long userId);

  boolean hasActiveSubscription(Long userId);
}