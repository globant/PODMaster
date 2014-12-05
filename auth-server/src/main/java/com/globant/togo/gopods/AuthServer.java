package com.globant.togo.gopods;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
//import org.springframework.hateoas.UriTemplate;
//import org.springframework.hateoas.hal.CurieProvider;
//import org.springframework.hateoas.hal.DefaultCurieProvider;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 * Spring Boot application.
 */
public class AuthServer extends SpringBootServletInitializer {
//  public static final String CURIE_NAMESPACE = "podmaster";

  /**
   * Bootstraps the application in standalone mode (i.e. java -jar).
   * 
   * @param args main arguments.
   */
  public static void main(String[] args) {
    SpringApplication.run(WebConfiguration.class, args);
  }

  /**
   * Allows the application to be started when being deployed into a Servlet 3
   * container.
   * 
   * @see org.springframework.boot.web.SpringBootServletInitializer#
   * configure(org.springframework.boot.builder.SpringApplicationBuilder)
   */
  @SuppressWarnings("javadoc")
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(WebConfiguration.class);
  }

  /**
   * Application Configuration. 
   */
  @Configuration
  @EnableAsync
  @EnableAutoConfiguration
  @ComponentScan(includeFilters = @Filter(Service.class), useDefaultFilters = false)
  static class ApplicationConfiguration {
  }

  /**
   * Web specific configuration.
   * 
   * @author Andres Postiglioni
   */
  @Configuration
  @Import({ ApplicationConfiguration.class })
  @ComponentScan(excludeFilters = @Filter({ Service.class }))
  static class WebConfiguration {
  }
}