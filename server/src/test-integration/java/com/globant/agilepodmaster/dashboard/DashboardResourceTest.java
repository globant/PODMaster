package com.globant.agilepodmaster.dashboard;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

import java.net.URI;

import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.globant.agilepodmaster.AbstractIntegrationTest;

public class DashboardResourceTest extends AbstractIntegrationTest {
  private static final String BASE_URL = "http://localhost:" + SERVER_PORT + "/dashboards";

  @Test
  public void shouldCreateFollowAndDelete() {
    String requestJson = DashboardJsonFixtures.createDashboard(Dashboard.DashboardType.Account)
        .withWidget("Widget Title", "Widget Name").buildJson();

    RestTemplate rest = new TestRestTemplate();

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
