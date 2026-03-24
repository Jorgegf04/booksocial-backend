package com.example.booksocial_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.user.Role;
import com.example.booksocial_backend.domain.user.User;

/**
 * Repositorio JPA para la gestión de persistencia de la entidad {@link User}.
 *
 * Proporciona operaciones CRUD básicas mediante Spring Data JPA,
 * además de consultas personalizadas necesarias para autenticación y gestión de
 * usuarios.
 * 
 * @author: Jorge
 * @since: 12/03/2026
 * @version: 1.1
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Busco un usuario por username.
   * Usado por Spring Security.
   */
  Optional<User> findByUsername(String username);

  /**
   * Busco un usuario por email.
   */
  Optional<User> findByEmail(String email);

  /**
   * Compruebo si existe un username.
   */
  boolean existsByUsername(String username);

  /**
   * Compruebo si existe un email.
   */
  boolean existsByEmail(String email);

  /**
   * Busco usuarios por nombre parcial.
   */
  List<User> findByUsernameContainingIgnoreCase(String username);

  /**
   * Obtengo usuarios activos.
   */
  List<User> findByActiveTrue();

  /**
   * Obtengo usuarios inactivos.
   */
  List<User> findByActiveFalse();

  /**
   * Busco usuarios por rol.
   */
  List<User> findByRole(Role role);

  /**
   * Cuenta usuarios activos.
   */
  @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
  Long countActiveUsers();

  /**
   * Obtengo usuarios ordenados por fecha de registro.
   */
  @Query("SELECT u FROM User u ORDER BY u.registrationDate DESC")
  List<User> findAllOrderByRegistrationDateDesc();

  /**
   * Búsqueda por email parcial.
   */
  @Query("""
          SELECT u FROM User u
          WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))
      """)
  List<User> searchByEmail(String email);
}
