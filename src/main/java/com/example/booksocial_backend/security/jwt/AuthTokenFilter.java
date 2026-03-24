package com.example.booksocial_backend.security.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.booksocial_backend.security.service.UserDetailsServiceImpl;

import java.io.IOException;

/**
 * Filtro de autenticación JWT.
 *
 * Intercepta todas las peticiones HTTP y:
 * - Extrae el token JWT de la cabecera Authorization.
 * - Valida el token.
 * - Carga el usuario en el contexto de seguridad.
 *
 * Permite que el sistema funcione sin sesiones (stateless).
 *
 * @author Jorge
 * @since 2026
 * @version 1.0
 */
public class AuthTokenFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    try {

      String jwt = parseJwt(request);

      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

        String username = jwtUtils.getUsernameFromJwtToken(jwt);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    filterChain.doFilter(request, response);
  }

  /**
   * Extrae el token JWT de la cabecera HTTP Authorization.
   */
  private String parseJwt(HttpServletRequest request) {

    String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      return header.substring(7);
    }

    return null;
  }
}