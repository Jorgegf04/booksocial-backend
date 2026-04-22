package com.example.booksocial_backend.cucumber;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
/**
 * Configuración del contexto Spring para los tests de aceptación Cucumber.
 *
 * <p>Arranca el servidor en un puerto aleatorio ({@code WebEnvironment.RANDOM_PORT})
 * para que los steps puedan realizar llamadas HTTP reales a la API REST sin colisionar
 * con ningún puerto fijo. Usa H2 en memoria ({@code create-drop}) para garantizar
 * aislamiento entre ejecuciones de la suite.</p>
 *
 * <p>La anotación {@link io.cucumber.spring.CucumberContextConfiguration} indica a
 * Cucumber que este es el punto de entrada para obtener el contexto de Spring,
 * permitiendo que los steps con {@code @Autowired} funcionen correctamente.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
public class SpringContextConfig {

  @LocalServerPort
  int port;

  public int getPort() {
    return port;
  }
}