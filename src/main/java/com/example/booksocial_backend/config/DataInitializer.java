package com.example.booksocial_backend.config;

import java.time.LocalDate;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.booksocial_backend.domain.user.Role;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Inicializa datos esenciales al arrancar la aplicación.
 *
 * Si no existe ningún usuario ADMIN en la BD, crea uno por defecto:
 *   username : admin
 *   password : admin123
 *
 * Este comportamiento es idempotente — si el admin ya existe, no hace nada.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(ApplicationArguments args) {

    if (!userRepository.existsByUsername("admin")) {
      User admin = User.builder()
          .username("admin")
          .email("admin@booksocial.com")
          .password(passwordEncoder.encode("admin123"))
          .name("Admin")
          .secondName("BookSocial")
          .role(Role.ADMIN)
          .active(true)
          .registrationDate(LocalDate.now())
          .build();

      userRepository.save(admin);
      log.info("==> Usuario admin creado. username=admin / password=admin123");
    } else {
      log.info("==> Usuario admin ya existe en la BD.");
    }
  }
}
