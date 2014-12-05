package com.globant.agilepodmaster.security.oauth2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.ResourceUtils;

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableWebMvcSecurity
public class OAuthSecurityConfiguration extends ResourceServerConfigurerAdapter {
  private static final String RESOURCE_ID = "restservice";

  @Value("${podmaster.auth-server.public-key:file:./config/auth-server-id_rsa.pub}")
  private String publicKey;

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    // @formatter:off
    resources.resourceId(RESOURCE_ID);
    // @formatter:on
  }

  @Bean
  @Primary
  public ResourceServerTokenServices tokenServices() throws Exception {
    DefaultTokenServices tokenServices = new DefaultTokenServices();
    tokenServices.setTokenStore(new JwtTokenStore(jwtTokenEnhancer()));
    return tokenServices;
  }
  
  @Bean
  public JwtAccessTokenConverter jwtTokenEnhancer() throws Exception {
    JwtAccessTokenConverter jwtTokenConverter = new JwtAccessTokenConverter();
    jwtTokenConverter.setVerifierKey(this.readFile(publicKey));
    return jwtTokenConverter;
  }

  private String readFile(String resourceLocation) throws FileNotFoundException {
    Scanner scanner = null;
    try {
      File resourceFile = ResourceUtils.getFile(resourceLocation);
      scanner = new Scanner(resourceFile, "UTF-8");
      scanner.useDelimiter("\\Z");
      
      return scanner.next();
    } finally {
      if (scanner != null) {
        scanner.close();
      }
    }
  }  
  @Override
  public void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
    .authorizeRequests()
      .antMatchers("/snapshots/**").fullyAuthenticated();
    // @formatter:on
  }
}
