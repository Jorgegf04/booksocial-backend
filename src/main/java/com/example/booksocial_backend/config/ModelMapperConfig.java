package com.example.booksocial_backend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración global de ModelMapper.
 */
@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}