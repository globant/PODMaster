package com.globant.agilepodmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

public class AgilePodMaster extends SpringBootServletInitializer {
  public static final String CURIE_NAMESPACE = "podmaster";

  /**
   * Bootstraps the application in standalone mode (i.e. java -jar).
   * 
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(WebConfiguration.class, args);
  }

  /**
   * Allows the application to be started when being deployed into a Servlet 3
   * container.
   * 
   * @see org.springframework.boot.web.SpringBootServletInitializer#
   *      configure(org.springframework.boot.builder.SpringApplicationBuilder)
   */
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(WebConfiguration.class);
  }

  @Configuration
  @EnableAsync
  @EnableAutoConfiguration
  @ComponentScan(includeFilters = @Filter(Service.class), useDefaultFilters = false)
  static class ApplicationConfiguration {
    @Bean
    public RestTemplate restTemplate() {
      HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
      factory.setReadTimeout(50000);
      factory.setConnectTimeout(15000);
      
      RestTemplate restTemplate = new RestTemplate(factory);

      return restTemplate;
    }
  }

  /**
   * Web specific configuration.
   * 
   * @author Andres Postiglioni
   */
  @Configuration
  @Import({ ApplicationConfiguration.class })
  @ComponentScan(excludeFilters = @Filter({ Service.class, Configuration.class }))
  static class WebConfiguration {

    @Bean
    public CurieProvider curieProvider() {
      return new DefaultCurieProvider(CURIE_NAMESPACE, new UriTemplate(
          "http://localhost:8080/alps/{rel}"));
    }
  }
}
