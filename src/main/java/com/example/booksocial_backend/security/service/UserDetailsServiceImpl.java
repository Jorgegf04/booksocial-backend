package com.example.booksocial_backend.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.repository.UserRepository;

/**
 * Implementación de {@link UserDetailsService} utilizada por Spring Security
 * para cargar los detalles de un usuario a partir de su nombre de usuario.
 *
 * <p>Este servicio es invocado automáticamente por el mecanismo de autenticación
 * de Spring Security durante el proceso de login. Recupera el usuario de la base
 * de datos y construye un objeto {@link UserDetailsImpl} que contiene las
 * credenciales y los roles necesarios para la autorización.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return UserDetailsImpl.build(user);
  }
}