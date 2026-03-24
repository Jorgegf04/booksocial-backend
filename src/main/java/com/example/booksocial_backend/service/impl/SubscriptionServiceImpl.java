package com.example.booksocial_backend.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.user.ActivateSubscriptionRequest;
import com.example.booksocial_backend.DTO.user.SubscriptionDTO;
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
  public SubscriptionDTO activateSubscription(ActivateSubscriptionRequest request) {

    // 🔥 Evitar duplicados
    if (subscriptionRepository.existsByUserIdAndActivatedTrue(request.userId())) {
      throw new IllegalArgumentException("El usuario ya tiene una suscripción activa");
    }

    Subscription subscription = new Subscription();

    subscription.setStartDate(LocalDate.now());
    subscription.setEndDate(LocalDate.now().plusDays(30));
    subscription.setActivated(true);

    User user = new User();
    user.setId(request.userId());
    subscription.setUser(user);

    Subscription saved = subscriptionRepository.save(subscription);

    return mapToDTO(saved);
  }

  @Override
  public void cancelSubscription(Long userId) {

    Subscription subscription = subscriptionRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));

    subscription.setActivated(false);
  }

  @Override
  @Transactional(readOnly = true)
  public SubscriptionDTO getSubscriptionByUserId(Long userId) {

    Subscription subscription = subscriptionRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));

    return mapToDTO(subscription);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean hasActiveSubscription(Long userId) {
    return subscriptionRepository.existsByUserIdAndActivatedTrue(userId);
  }

  private SubscriptionDTO mapToDTO(Subscription subscription) {

    return new SubscriptionDTO(
        subscription.getId(),
        subscription.getStartDate(),
        subscription.getEndDate(),
        subscription.getActivated(),
        subscription.getUser().getId());
  }
}