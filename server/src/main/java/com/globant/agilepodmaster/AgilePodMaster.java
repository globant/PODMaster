package com.globant.agilepodmaster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Spring Boot application.
 */
public class AgilePodMaster extends SpringBootServletInitializer {
  public static final String CURIE_NAMESPACE = "podmaster";

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
  @ComponentScan(
      includeFilters = @Filter({ Service.class, Repository.class }), 
      useDefaultFilters = false)
  static class ApplicationConfiguration {
  }

  /**
   * Web specific configuration.
   * 
   * @author Andres Postiglioni
   */
  @Configuration
  @Import({ ApplicationConfiguration.class })
  @ComponentScan(excludeFilters = @Filter({ Service.class, Repository.class }))
  static class WebConfiguration {
    @Bean
    public RestTemplate restTemplate() {
      HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
      factory.setReadTimeout(100000);
      factory.setConnectTimeout(50000);
      
      RestTemplate restTemplate = new RestTemplate(factory);

      return restTemplate;
    }

    @Bean
    public ConversionServiceFactoryBean conversionServiceFactory(List<Converter<?,?>> converters) {
      ConversionServiceFactoryBean conversionServiceFactory = new ConversionServiceFactoryBean();

      Set<Object> set = new HashSet<Object>();
      set.addAll(converters);
      conversionServiceFactory.setConverters(set);

      return conversionServiceFactory;
    }

    @Bean
    public CurieProvider curieProvider() {
      return new DefaultCurieProvider(CURIE_NAMESPACE, new UriTemplate(
          "http://localhost:8080/alps/{rel}"));
    }
  }
}