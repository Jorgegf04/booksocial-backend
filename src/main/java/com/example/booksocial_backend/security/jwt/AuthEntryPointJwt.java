package com.example.booksocial_backend.security.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Clase encargada de gestionar errores de autenticación.
 *
 * Se ejecuta cuando un usuario intenta acceder a un recurso protegido
 * sin estar autenticado o con un token inválido.
 *
 * Devuelve un error HTTP 401 (Unauthorized).
 *
 * @author Jorge
 * @since 2026
 * @version 1.0
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {

    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
  }
}