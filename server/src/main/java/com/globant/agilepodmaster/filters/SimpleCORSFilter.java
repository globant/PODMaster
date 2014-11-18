package com.globant.agilepodmaster.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimpleCORSFilter implements Filter {

  @Value("${cors.access-control-allow-origin:}")
  private String origin;

  @Value("${cors.access-control-allow-methods:}")
  private String methods;

  @Value("${cors.access-control-max-age:}")
  private String maxAge;

  @Value("${cors.access-control-allow-headers:}")
  private String headers;

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
    throws IOException, ServletException {
    
    HttpServletResponse response = (HttpServletResponse) res;

    this.setResponseHeader(response, "Access-Control-Allow-Origin", origin);
    this.setResponseHeader(response, "Access-Control-Allow-Methods", methods);
    this.setResponseHeader(response, "Access-Control-Max-Age", maxAge);
    this.setResponseHeader(response, "Access-Control-Allow-Headers", headers);

    chain.doFilter(req, res);
  }

  private void setResponseHeader(HttpServletResponse response, String header, String value) {
    if (value != null && !"".equals(value.trim())) {
      response.setHeader(header, value);
    }
  }

  public void init(FilterConfig filterConfig) {
  }

  public void destroy() {
  }
}