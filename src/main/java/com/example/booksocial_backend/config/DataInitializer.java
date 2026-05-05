package com.example.booksocial_backend.config;

import java.time.LocalDate;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.user.Role;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("dev")
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        @Transactional
        public void run(ApplicationArguments args) {

                log.info("[DataInitializer] Inicializando datos mínimos...");

                seedAdmin();

                seedUser(
                                "user1",
                                "user@test.com",
                                "User",
                                "Demo",
                                Role.SUBSCRIBED);

                log.info("[DataInitializer] OK");
        }

        // ─────────────────────────────────────────────
        // USERS
        // ─────────────────────────────────────────────

        private void seedAdmin() {
                if (!userRepository.existsByUsername("admin")) {
                        userRepository.save(User.builder()
                                        .username("admin")
                                        .email("admin@booksocial.com")
                                        .password(passwordEncoder.encode("admin123"))
                                        .name("Admin")
                                        .secondName("System")
                                        .role(Role.ADMIN)
                                        .active(true)
                                        .registrationDate(LocalDate.now())
                                        .img("")
                                        .build());

                        log.info("✔ Admin creado");
                }
        }

        private void seedUser(String username, String email, String name,
                        String secondName, Role role) {

                if (userRepository.existsByUsername(username)) {
                        return;
                }

                userRepository.save(User.builder()
                                .username(username)
                                .email(email)
                                .name(name)
                                .secondName(secondName)
                                .img("")
                                .role(role)
                                .active(true)
                                .registrationDate(LocalDate.now())
                                .password(passwordEncoder.encode("password123"))
                                .build());

                log.info("✔ Usuario {} creado", username);
        }
}