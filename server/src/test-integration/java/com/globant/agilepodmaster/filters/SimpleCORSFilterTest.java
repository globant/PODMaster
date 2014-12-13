package com.globant.agilepodmaster.filters;

import static com.globant.agilepodmaster.ssl.SSLTestHelper.createNotVerifyingTestRestTemplate;
import static com.globant.agilepodmaster.ssl.SSLTestHelper.restoreSSLContext;
import static com.globant.agilepodmaster.ssl.SSLTestHelper.trustSelfSignedSSL;
import static org.hamcrest.Matchers.contains;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.globant.agilepodmaster.AbstractIntegrationTest;

/**
 * Test for SimpleCORSFilter.
 * 
 * @author Andres Postiglioni.
 */
public class SimpleCORSFilterTest extends AbstractIntegrationTest {
  @BeforeClass
  public static void beforeClass() {
    trustSelfSignedSSL();
  }

  @AfterClass
  public static void afterClass() {
    restoreSSLContext();
  }

  /**
   * Test if a response header header has the right property to deal with CORS.
   */
  @Test
  public void shouldCreateFollowAndDelete() {
    final String BASE_URL = super.buildServerUrl("/dashboards");

    RestTemplate rest = createNotVerifyingTestRestTemplate();

    ResponseEntity<String> response = rest.getForEntity(BASE_URL, String.class);
    HttpHeaders headers = response.getHeaders();

    assertThat(headers.get("Access-Control-Allow-Origin"), contains("*"));
  }
}
