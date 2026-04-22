package com.example.booksocial_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación backend de BookSocial.
 *
 * <p>Arranca el contexto de Spring Boot, inicializa la configuración
 * de seguridad JWT, JPA/Hibernate y los beans de la aplicación.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
@SpringBootApplication
public class BooksocialBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksocialBackendApplication.class, args);
	}

}
