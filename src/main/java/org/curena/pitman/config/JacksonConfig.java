package org.curena.pitman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Configuration class for Jackson ObjectMapper.
 * Provides a properly configured ObjectMapper bean that can handle Java 8 date/time types.
 */
@Configuration
public class JacksonConfig {

  /**
   * Creates an ObjectMapper bean with JavaTimeModule registered.
   *
   * @return The ObjectMapper bean
   */
  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }
}
