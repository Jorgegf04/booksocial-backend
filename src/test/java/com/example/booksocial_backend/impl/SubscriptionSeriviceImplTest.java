package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import com.example.booksocial_backend.DTO.user.SubscriptionRequestDTO;
import com.example.booksocial_backend.domain.user.Subscription;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.repository.SubscriptionRepository;
import com.example.booksocial_backend.service.impl.SubscriptionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
/**
 * Tests unitarios de {@link com.example.booksocial_backend.service.impl.SubscriptionServiceImpl}.
 *
 * <p>Verifica la activación de suscripciones (30 días desde hoy) con rechazo de suscripciones
 * ya activas, la cancelación con persistencia del estado {@code activated=false}, la consulta
 * por usuario y la comprobación de suscripción activa mediante
 * {@code hasActiveSubscription}.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
class SubscriptionServiceImplTest {

  @Mock
  private SubscriptionRepository subscriptionRepository;

  @InjectMocks
  private SubscriptionServiceImpl service;

  private Subscription subscription;

  @BeforeEach
  void setUp() {

    User user = new User();
    user.setId(1L);

    subscription = new Subscription();
    subscription.setId(1L);
    subscription.setUser(user);
    subscription.setStartDate(LocalDate.now());
    subscription.setEndDate(LocalDate.now().plusDays(30));
    subscription.setActivated(true);
  }

  // =========================
  // ACTIVATE
  // =========================

  @Test
  void shouldActivateSubscriptionSuccessfully() {

    SubscriptionRequestDTO request = new SubscriptionRequestDTO(1L);

    when(subscriptionRepository.existsByUserIdAndActivatedTrue(1L)).thenReturn(false);
    when(subscriptionRepository.save(any())).thenReturn(subscription);

    var result = service.activateSubscription(request);

    assertTrue(result.getActivated());
    assertEquals(1L, result.getUserId());
  }

  @Test
  void shouldThrowExceptionWhenSubscriptionAlreadyExists() {

    SubscriptionRequestDTO request = new SubscriptionRequestDTO(1L);

    when(subscriptionRepository.existsByUserIdAndActivatedTrue(1L)).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> {
      service.activateSubscription(request);
    });
  }

  // =========================
  // CANCEL
  // =========================

  @Test
  void shouldCancelSubscription() {

    when(subscriptionRepository.findByUserId(1L))
        .thenReturn(Optional.of(subscription));

    service.cancelSubscription(1L);

    assertFalse(subscription.getActivated());
  }

  @Test
  void shouldThrowExceptionWhenSubscriptionNotFoundOnCancel() {

    when(subscriptionRepository.findByUserId(1L))
        .thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      service.cancelSubscription(1L);
    });
  }

  // =========================
  // GET
  // =========================

  @Test
  void shouldGetSubscriptionByUserId() {

    when(subscriptionRepository.findByUserId(1L))
        .thenReturn(Optional.of(subscription));

    var result = service.getSubscriptionByUserId(1L);

    assertEquals(1L, result.getUserId());
    assertTrue(result.getActivated());
  }

  @Test
  void shouldThrowExceptionWhenSubscriptionNotFound() {

    when(subscriptionRepository.findByUserId(1L))
        .thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      service.getSubscriptionByUserId(1L);
    });
  }

  // =========================
  // ACTIVE CHECK
  // =========================

  @Test
  void shouldReturnTrueWhenUserHasActiveSubscription() {

    when(subscriptionRepository.existsByUserIdAndActivatedTrue(1L))
        .thenReturn(true);

    boolean result = service.hasActiveSubscription(1L);

    assertTrue(result);
  }

  @Test
  void shouldReturnFalseWhenUserHasNoActiveSubscription() {

    when(subscriptionRepository.existsByUserIdAndActivatedTrue(1L))
        .thenReturn(false);

    boolean result = service.hasActiveSubscription(1L);

    assertFalse(result);
  }
}