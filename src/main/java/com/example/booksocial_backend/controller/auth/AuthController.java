package com.example.booksocial_backend.controller.auth;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.booksocial_backend.DTO.auth.*;
import com.example.booksocial_backend.security.jwt.JwtUtils;
import com.example.booksocial_backend.service.UserService;

/**
 * Controlador encargado de la autenticación de usuarios.
 *
 * Gestiona:
 * - Registro de nuevos usuarios.
 * - Inicio de sesión.
 * - Generación de tokens JWT.
 *
 * Forma parte del sistema de seguridad basado en JWT,
 * permitiendo autenticación stateless.
 *
 * @author Jorge
 * @since 2026
 * @version 1.0
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final UserService userService;

  /**
   * Endpoint de login.
   *
   * Autentica al usuario y genera un token JWT.
   *
   * @param request credenciales del usuario
   * @return token JWT
   */
  /**
   * Login de usuario.
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = jwtUtils.generateJwtToken(authentication);

    return ResponseEntity.ok(new JwtResponse(jwt, "Bearer"));
  }

  /**
   * Endpoint de registro.
   *
   * Crea un nuevo usuario en el sistema.
   *
   * @param request datos del usuario
   * @return mensaje de confirmación
   */
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

    userService.registerUser(request);

    return ResponseEntity.ok("Usuario registrado correctamente");
  }
}