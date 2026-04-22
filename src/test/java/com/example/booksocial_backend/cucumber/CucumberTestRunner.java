package com.example.booksocial_backend.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * Runner de los tests de aceptación Cucumber para BookSocial.
 *
 * <p>Descubre los ficheros {@code .feature} en el classpath bajo {@code features/}
 * y ejecuta los steps definidos en el paquete
 * {@code com.example.booksocial_backend.cucumber}.</p>
 *
 * <p>Para ejecutar solo los tests de Cucumber usar el perfil Maven {@code cucumber}
 * o lanzar esta clase directamente como JUnit Suite.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
@Suite
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.example.booksocial_backend.cucumber")
public class CucumberTestRunner {
}