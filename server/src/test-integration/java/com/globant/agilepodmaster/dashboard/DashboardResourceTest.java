package com.globant.agilepodmaster.dashboard;

import static com.globant.agilepodmaster.ssl.SSLTestHelper.createNotVerifyingTestRestTemplate;
import static com.globant.agilepodmaster.ssl.SSLTestHelper.restoreSSLContext;
import static com.globant.agilepodmaster.ssl.SSLTestHelper.trustSelfSignedSSL;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

import java.net.URI;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.globant.agilepodmaster.AbstractIntegrationTest;

/**
 * Integrity test for service.
 * 
 * @author Andres Postiglioni.
 *
 */
public class DashboardResourceTest extends AbstractIntegrationTest {
  @BeforeClass
  public static void beforeClass() {
    trustSelfSignedSSL();
  }

  @AfterClass
  public static void afterClass() {
    restoreSSLContext();
  }

  /**
   * Inserts through the service a dashboard to the db and then remove it using
   * also the service. Finally check through the service that the dashboard does
   * not exist.
   */
  @Test
  public void shouldCreateFollowAndDelete() {
    final String BASE_URL = super.buildServerUrl("/dashboards");

    String requestJson = DashboardJsonFixtures.createDashboard(Dashboard.DashboardType.Account)
        .withWidget("Widget Title", "Widget Name").buildJson();

    RestTemplate rest = createNotVerifyingTestRestTemplate();

    // Post new Dashboard
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> postData = new HttpEntity<String>(requestJson, headers);

    ResponseEntity<Dashboard> postResponse = rest
        .postForEntity(BASE_URL, postData, Dashboard.class);
    assertThat(postResponse.getStatusCode(), equalTo(HttpStatus.CREATED));
    assertThat(postResponse.getBody(), nullValue());

    URI location = postResponse.getHeaders().getLocation();
    assertThat(location, notNullValue());
    assertThat(location.toString(), startsWith(BASE_URL));

    // Follow location
    ResponseEntity<String> getResponse = rest.getForEntity(location.toString(), String.class);
    MediaType contentType = getResponse.getHeaders().getContentType();
    assertThat(contentType, equalTo(MediaType.parseMediaType("application/json;charset=UTF-8")));

    System.out.println("XXXX>" + getResponse.getBody());
    // TODO: Assert that body matches the expected json

    // Delete
    rest.delete(location);

    // Ensure it's not longer present
    ResponseEntity<String> getAfterDelete = rest.getForEntity(location.toString(), String.class);
    assertThat(getAfterDelete.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
  }
}
