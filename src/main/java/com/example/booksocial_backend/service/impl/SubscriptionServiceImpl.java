package com.example.booksocial_backend.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.user.SubscriptionRequestDTO;
import com.example.booksocial_backend.DTO.user.SubscriptionResponseDTO;
import com.example.booksocial_backend.domain.user.Subscription;
import com.example.booksocial_backend.domain.user.User;

import com.example.booksocial_backend.repository.SubscriptionRepository;
import com.example.booksocial_backend.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;

  @Override
  public SubscriptionResponseDTO activateSubscription(SubscriptionRequestDTO request) {

    if (subscriptionRepository.existsByUserIdAndActivatedTrue(request.getUserId())) {
      throw new IllegalArgumentException("El usuario ya tiene una suscripción activa");
    }

    Subscription subscription = new Subscription();

    subscription.setStartDate(LocalDate.now());
    subscription.setEndDate(LocalDate.now().plusDays(30));
    subscription.setActivated(true);

    User user = new User();
    user.setId(request.getUserId());
    subscription.setUser(user);

    Subscription saved = subscriptionRepository.save(subscription);

    return mapToDTO(saved);
  }

  @Override
  public void cancelSubscription(Long userId) {

    Subscription subscription = subscriptionRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("Suscripción no encontrada para el usuario: " + userId));

    subscription.setActivated(false);
  }

  @Override
  @Transactional(readOnly = true)
  public SubscriptionResponseDTO getSubscriptionByUserId(Long userId) {

    Subscription subscription = subscriptionRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("Suscripción no encontrada para el usuario: " + userId));

    return mapToDTO(subscription);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean hasActiveSubscription(Long userId) {
    return subscriptionRepository.existsByUserIdAndActivatedTrue(userId);
  }

  private SubscriptionResponseDTO mapToDTO(Subscription subscription) {
    return new SubscriptionResponseDTO(
        subscription.getId(),
        subscription.getStartDate(),
        subscription.getEndDate(),
        subscription.getActivated(),
        subscription.getUser() != null ? subscription.getUser().getId() : null);
  }
}