package com.example.booksocial_backend.service.impl;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.DTO.user.SubscriptionRequestDTO;
import com.example.booksocial_backend.DTO.user.SubscriptionResponseDTO;
import com.example.booksocial_backend.domain.user.Role;
import com.example.booksocial_backend.domain.user.Subscription;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.exception.SubscriptionAlreadyActiveException;
import com.example.booksocial_backend.exception.SubscriptionNotFoundException;
import com.example.booksocial_backend.exception.UserNotFoundException;
import com.example.booksocial_backend.repository.SubscriptionRepository;
import com.example.booksocial_backend.repository.UserRepository;
import com.example.booksocial_backend.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

  private static final Logger log = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

  private final SubscriptionRepository subscriptionRepository;
  private final UserRepository userRepository;

  @Override
  public SubscriptionResponseDTO activateSubscription(SubscriptionRequestDTO request) {

    log.info("[SUBSCRIPTION] [ACTIVATE] [START] userId={}", request.getUserId());
    if (subscriptionRepository.existsByUserIdAndActivatedTrue(request.getUserId())) {
      throw new SubscriptionAlreadyActiveException(request.getUserId());
    }

    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

    user.setRole(Role.SUBSCRIBED);
    userRepository.save(user);

    // Reutilizar el registro existente (puede estar inactivo por cancelación previa)
    // para no violar la constraint UNIQUE en user_id
    Subscription subscription = subscriptionRepository.findByUserId(request.getUserId())
        .orElse(new Subscription());

    subscription.setStartDate(LocalDate.now());
    subscription.setEndDate(LocalDate.now().plusDays(30));
    subscription.setActivated(true);
    subscription.setUser(user);

    Subscription saved = subscriptionRepository.save(subscription);

    log.info("[SUBSCRIPTION] [ACTIVATE] [SUCCESS] userId={}", request.getUserId());
    return mapToDTO(saved);
  }

  @Override
  public void cancelSubscription(Long userId) {

    log.info("[SUBSCRIPTION] [CANCEL] [START] userId={}", userId);
    Subscription subscription = subscriptionRepository.findByUserId(userId)
        .orElseThrow(() -> new SubscriptionNotFoundException(userId));

    subscription.setActivated(false);

    User user = subscription.getUser();
    user.setRole(Role.REGISTERED);
    userRepository.save(user);

    log.info("[SUBSCRIPTION] [CANCEL] [SUCCESS] userId={}", userId);
  }

  @Override
  @Transactional(readOnly = true)
  public SubscriptionResponseDTO getSubscriptionByUserId(Long userId) {

    Subscription subscription = subscriptionRepository.findByUserId(userId)
        .orElseThrow(() -> new SubscriptionNotFoundException(userId));

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