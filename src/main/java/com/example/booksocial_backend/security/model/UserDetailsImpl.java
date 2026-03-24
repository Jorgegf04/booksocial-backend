package com.example.booksocial_backend.security.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.booksocial_backend.domain.user.User;

/**
 * Implementación de UserDetails utilizada por Spring Security.
 *
 * Esta clase adapta la entidad {@link User} del sistema
 * al modelo de seguridad de Spring.
 *
 * Contiene:
 * - Credenciales del usuario (username y password)
 * - Roles convertidos en autoridades
 *
 * Es utilizada durante el proceso de autenticación
 * y en la validación de tokens JWT.
 *
 * @author Jorge
 * @since 2026
 * @version 1.0
 */
public class UserDetailsImpl implements UserDetails {

  private String username;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;

  /**
   * Constructor principal.
   */
  public UserDetailsImpl(String username, String password,
      Collection<? extends GrantedAuthority> authorities) {
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }

  /**
   * Construye un objeto UserDetailsImpl a partir de la entidad User.
   *
   * Convierte el rol del usuario en una autoridad reconocida
   * por Spring Security.
   *
   * @param user entidad de usuario
   * @return instancia de UserDetailsImpl
   */
  public static UserDetailsImpl build(User user) {

    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

    return new UserDetailsImpl(
        user.getUsername(),
        user.getPassword(),
        authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  /**
   * Devuelve el username utilizado para autenticación.
   */
  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * Indica si la cuenta está bloqueada.
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * Indica si el usuario está activo.
   */
  @Override
  public boolean isEnabled() {
    return true;
  }
}