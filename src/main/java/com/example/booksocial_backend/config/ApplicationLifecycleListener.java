package com.example.booksocial_backend.config;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.EntityType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

/**
 * Listener del ciclo de vida de la aplicación.
 * Loggea información relevante al arrancar y al parar.
 */
@Component
public class ApplicationLifecycleListener {

  private static final Logger log = LoggerFactory.getLogger(ApplicationLifecycleListener.class);

  private final Environment env;
  private final EntityManagerFactory emf;

  public ApplicationLifecycleListener(Environment env, EntityManagerFactory emf) {
    this.env = env;
    this.emf = emf;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady(ApplicationReadyEvent event) {
    String appName = env.getProperty("spring.application.name", "booksocial-backend");
    String port = env.getProperty("server.port", "9999");
    String[] profiles = env.getActiveProfiles();
    String activeProfiles = profiles.length == 0 ? "default" : Arrays.toString(profiles);
    String ddlAuto = env.getProperty("spring.jpa.hibernate.ddl-auto", "none");

    Set<EntityType<?>> entities = emf.getMetamodel().getEntities();

    log.info("=================================================================");
    log.info("  {} arrancado correctamente", appName);
    log.info("  Puerto      : {}", port);
    log.info("  Perfiles    : {}", activeProfiles);
    log.info("  ddl-auto    : {}", ddlAuto);
    log.info("  Entidades JPA gestionadas: {}", entities.size());
    log.info("  Swagger UI  : http://localhost:{}/swagger-ui/index.html", port);
    log.info("=================================================================");

    if ("update".equals(ddlAuto) || "create-drop".equals(ddlAuto)) {
      log.warn("[CONFIG] ddl-auto='{}' detectado — NO usar en producción", ddlAuto);
    }

    String jwtSecret = env.getProperty("jwt.secret", "");
    if (jwtSecret.isBlank()) {
      log.warn("[CONFIG] jwt.secret no configurado — usando valor por defecto inseguro");
    }

    String dbPassword = env.getProperty("spring.datasource.password", "");
    if (dbPassword.isBlank()) {
      log.warn("[CONFIG] spring.datasource.password está vacío");
    }
  }

  @PreDestroy
  public void onShutdown() {
    log.info("=================================================================");
    log.info("  BookSocial backend detenido limpiamente");
    log.info("=================================================================");
  }
}
