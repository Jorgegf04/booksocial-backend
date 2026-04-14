package com.example.booksocial_backend.cucumber;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

public class WorkSteps {

  @Autowired
  private SpringContextConfig ctx;

  @Autowired
  private TestRestTemplate restTemplate;

  // Estado compartido dentro del escenario
  // Los casts son seguros: TestRestTemplate deserializa JSON en Map/List en tiempo de ejecución
  @SuppressWarnings("rawtypes")
  private ResponseEntity<Map>   lastResponse;
  @SuppressWarnings("rawtypes")
  private ResponseEntity<List>  listResponse;
  private String                jwtToken;

  private String base() {
    return "http://localhost:" + ctx.getPort();
  }

  // ─────────────────────────────────────────────
  // AUTH — REGISTRO
  // ─────────────────────────────────────────────

  @When("me registro con username {string} email {string} y contraseña {string}")
  public void registro(String username, String email, String password) {
    Map<String, String> body = Map.of(
        "username", username,
        "email", email,
        "password", password);
    lastResponse = restTemplate.postForEntity(base() + "/api/auth/register", body, Map.class);
  }

  @Then("el registro devuelve estado {int}")
  public void registroEstado(int status) {
    assertEquals(status, lastResponse.getStatusCode().value());
  }

  // ─────────────────────────────────────────────
  // AUTH — LOGIN
  // ─────────────────────────────────────────────

  @Given("existe un usuario registrado con username {string} email {string} y contraseña {string}")
  public void existeUsuarioRegistrado(String username, String email, String password) {
    Map<String, String> body = Map.of(
        "username", username,
        "email", email,
        "password", password);
    // Ignoramos 4xx si el usuario ya existía de una ejecución anterior
    restTemplate.postForEntity(base() + "/api/auth/register", body, Map.class);
  }

  @When("inicio sesión con username {string} y contraseña {string}")
  public void loginConCredenciales(String username, String password) {
    Map<String, String> body = Map.of(
        "username", username,
        "password", password);
    lastResponse = restTemplate.postForEntity(base() + "/api/auth/login", body, Map.class);

    if (lastResponse.getStatusCode() == HttpStatus.OK && lastResponse.getBody() != null) {
      Object token = lastResponse.getBody().get("token");
      jwtToken = token != null ? token.toString() : null;
    }
  }

  @Then("recibo un token JWT no vacío")
  public void tokenNoVacio() {
    assertNotNull(jwtToken, "El token JWT no debería ser null");
    assertFalse(jwtToken.isBlank(), "El token JWT no debería estar vacío");
  }

  @Then("el tipo de token es {string}")
  public void tipoToken(String expectedType) {
    Object type = lastResponse.getBody().get("type");
    assertEquals(expectedType, type);
  }

  @Then("el login devuelve estado {int}")
  public void loginEstado(int status) {
    assertEquals(status, lastResponse.getStatusCode().value());
  }

  // ─────────────────────────────────────────────
  // WORKS — CREATE
  // ─────────────────────────────────────────────

  @When("creo una obra con título {string} tipo {string} y género {string}")
  public void creoObra(String title, String type, String genre) {
    Map<String, Object> body = Map.of(
        "title", title,
        "type", type,
        "genre", genre,
        "authors", List.of());
    lastResponse = restTemplate.postForEntity(base() + "/api/works", body, Map.class);
  }

  @Then("la respuesta de creación tiene estado {int}")
  public void creacionEstado(int status) {
    assertEquals(status, lastResponse.getStatusCode().value());
  }

  @Then("la obra devuelta tiene título {string}")
  public void obraConTitulo(String expectedTitle) {
    assertNotNull(lastResponse.getBody());
    assertEquals(expectedTitle, lastResponse.getBody().get("title"));
  }

  // ─────────────────────────────────────────────
  // WORKS — LIST
  // ─────────────────────────────────────────────

  @When("solicito el listado completo de obras")
  public void listarObras() {
    listResponse = restTemplate.getForEntity(base() + "/api/works", List.class);
  }

  @Then("la respuesta del listado tiene estado {int}")
  public void listadoEstado(int status) {
    assertEquals(status, listResponse.getStatusCode().value());
  }

  // ─────────────────────────────────────────────
  // WORKS — SEARCH
  // ─────────────────────────────────────────────

  @Given("existe una obra creada con título {string} tipo {string} y género {string}")
  public void existeObraCreada(String title, String type, String genre) {
    Map<String, Object> body = Map.of(
        "title", title,
        "type", type,
        "genre", genre,
        "authors", List.of());
    restTemplate.postForEntity(base() + "/api/works", body, Map.class);
  }

  @When("busco obras por título {string}")
  public void buscarPorTitulo(String title) {
    listResponse = restTemplate.getForEntity(
        base() + "/api/works/search?title=" + title, List.class);
  }

  @Then("la búsqueda devuelve al menos una obra")
  public void busquedaNoVacia() {
    assertNotNull(listResponse.getBody());
    assertFalse(listResponse.getBody().isEmpty(), "La búsqueda debería devolver al menos una obra");
  }

  @SuppressWarnings("unchecked")
  @Then("una de las obras tiene título {string}")
  public void unaObraConTitulo(String expectedTitle) {
    boolean found = listResponse.getBody().stream()
        .anyMatch(item -> {
          if (item instanceof Map) {
            Object t = ((Map<?, ?>) item).get("title");
            return expectedTitle.equals(t);
          }
          return false;
        });
    assertTrue(found, "No se encontró ninguna obra con título: " + expectedTitle);
  }
}