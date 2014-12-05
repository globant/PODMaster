package com.globant.togo.gopods.security.oauth2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import lombok.Cleanup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.ResourceUtils;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {
  private static final String RESOURCE_ID = "restservice";

  @Autowired
  @Qualifier("authenticationManagerBean")
  private AuthenticationManager authenticationManager;

  @Autowired
  private ClientDetailsService clientDetailsService;

  @Value("${podmaster.auth-server.private-key:file:./config/id_rsa}")
  private String privateKey;

  @Value("${podmaster.auth-server.public-key:file:./config/id_rsa.pub}")
  private String publicKey;

  @Override
  @SuppressWarnings("checkstyle:indentation")
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    // @formatter:off
    endpoints
      .accessTokenConverter(jwtTokenEnhancer())
      .authenticationManager(authenticationManager);
    // @formatter:on
  }

  @Bean
  public JwtAccessTokenConverter jwtTokenEnhancer() throws Exception {
    JwtAccessTokenConverter jwtTokenConverter = new JwtAccessTokenConverter();
    jwtTokenConverter.setSigningKey(this.readFile(privateKey));
    jwtTokenConverter.setVerifierKey(this.readFile(publicKey));
    return jwtTokenConverter;
  }

  private String readFile(String resourceLocation) throws FileNotFoundException {
    File resourceFile = ResourceUtils.getFile(resourceLocation);
    
    @Cleanup Scanner scanner = new Scanner(resourceFile, "UTF-8");
    scanner.useDelimiter("\\Z");

    return scanner.next();
  }

  @Override
  @SuppressWarnings("checkstyle:indentation")
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    // @formatter:off
    clients
      .inMemory()
        .withClient("clientapp")
        .secret("123456")
        .authorizedGrantTypes("password", "refresh_token")
        .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT").scopes("read", "write")
      .resourceIds(RESOURCE_ID);
    // @formatter:on
  }

  @Primary 
  @Bean
  public DefaultTokenServices tokenServices() throws Exception {
    DefaultTokenServices tokenServices = new DefaultTokenServices();
    tokenServices.setSupportRefreshToken(true);
    tokenServices.setTokenStore(new JwtTokenStore(jwtTokenEnhancer()));
    return tokenServices;
  }

  @SuppressWarnings("checkstyle:indentation")
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
    // @formatter:off
    oauthServer
      .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
      .tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
    // @formatter:on
  }
}
