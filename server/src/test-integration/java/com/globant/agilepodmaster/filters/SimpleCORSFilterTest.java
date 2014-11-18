package com.globant.agilepodmaster.filters;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.globant.agilepodmaster.AbstractIntegrationTest;

public class SimpleCORSFilterTest extends AbstractIntegrationTest {

  @Test
  public void shouldCreateFollowAndDelete() {
    final String BASE_URL = "http://localhost:" + this.getServerPort() + "/dashboards";
    RestTemplate rest = new TestRestTemplate();

    ResponseEntity<String> response = rest.getForEntity(BASE_URL, String.class);
    HttpHeaders headers = response.getHeaders();

    assertThat(headers.get("Access-Control-Allow-Origin"), contains("*"));
  }
}
