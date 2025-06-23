package org.curena.pitman.config;

import java.net.URISyntaxException;

import org.apache.hc.core5.http.HttpHost;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuration class for OpenSearch client. Provides a bean for the OpenSearchClient that can be
 * autowired into other components.
 */
@Configuration
public class OpenSearchConfig {

  @Value("${opensearch.host}")
  private String host;

  @Value("${opensearch.port}")
  private int port;

  @Value("${opensearch.scheme}")
  private String scheme;

  /**
   * Creates an OpenSearchClient bean.
   *
   * @return The OpenSearchClient bean
   * @throws RuntimeException if there's an error creating the HttpHost
   */
  @Bean
  public OpenSearchClient openSearchClient(final ObjectMapper objectMapper) {
    try {
      return new OpenSearchClient(
          ApacheHttpClient5TransportBuilder.builder(
                  HttpHost.create(scheme + "://" + host + ":" + port))
              .setMapper(new JacksonJsonpMapper(objectMapper))
              .build());
    } catch (URISyntaxException e) {
      throw new RuntimeException("Error creating OpenSearchClient: ", e);
    }
  }
}
