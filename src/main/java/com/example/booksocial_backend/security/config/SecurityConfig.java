package com.example.booksocial_backend.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.booksocial_backend.security.jwt.AuthEntryPointJwt;
import com.example.booksocial_backend.security.jwt.AuthTokenFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private AuthEntryPointJwt authEntryPointJwt;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Order(1)
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

    http.securityMatcher("/api/**");

    http
        .csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt))
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .authorizeHttpRequests(auth -> auth

            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/users/**").permitAll()
            .requestMatchers("/api/subscriptions/**").permitAll()
            .requestMatchers("/api/works/**").permitAll()
            .requestMatchers("/api/authors/**").permitAll()
            .requestMatchers("/api/chapters/**").permitAll()
            .requestMatchers("/api/tomes/**").permitAll()
            .requestMatchers("/api/comments/**").permitAll() // puedes quitarlo luego si quieres proteger
            .requestMatchers("/api/reactions/**").permitAll() // puedes quitarlo luego si quieres proteger
            .requestMatchers("/api/events/**").permitAll() // puedes quitarlo luego si quieres proteger
            .requestMatchers("/api/subscriptions/**").permitAll() // puedes quitarlo luego si quieres proteger
            .requestMatchers("/api/tracking-works/**").permitAll()

            .anyRequest().authenticated());

    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(),
        UsernamePasswordAuthenticationFilter.class);

    http.cors(Customizer.withDefaults());

    return http.build();
  }
}