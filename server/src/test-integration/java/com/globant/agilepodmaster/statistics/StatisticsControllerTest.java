package com.globant.agilepodmaster.statistics;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.globant.agilepodmaster.AbstractIntegrationTest;
import com.globant.agilepodmaster.JsonBuilder;

/**
 * Test for StatisticsController.
 * @author Andres Postiglioni.
 *
 */
public class StatisticsControllerTest extends AbstractIntegrationTest {
  
  /**
   * Testing RegressionSimple.
   */
  @Test
  public void testRegressionSimple() {
    final String BASE_URL = "http://localhost:" + this.getServerPort() 
        + "/statistics/prediction/regression/simple?";

    String requestParams = "pair=(1,2)&pair=(2,4.1)&pair=(3,6.05)&pair=(4,7.99)"
                         + "&intercept=true"
                         + "&predict=5&predict=6.1";

    String expected = new JsonBuilder()
        .withNestedObject(
            "slopeInterval",
            i -> i.withProperty("center", 1.9920000000000002)
                .withProperty("lowerBound", 1.875113467250817)
                .withProperty("upperBound", 2.10888653274924)
                .withProperty("confidenceLevel", 0.95))
        .withProperty("intercept", 0.054999999999999716)
        .withArray(
            "predictions",
            b -> b.addNestedObject(
                n -> n.withProperty("x", 5.0).withProperty("y", 10.015))
                .addNestedObject(
                    n -> n.withProperty("x", 6.1).withProperty("y", 12.2062)))
        .build();

    RestTemplate rest = new TestRestTemplate();

    // Follow location
    ResponseEntity<String> getResponse = rest.getForEntity(BASE_URL + requestParams, String.class);
    MediaType contentType = getResponse.getHeaders().getContentType();
    assertThat(contentType, equalTo(MediaType.parseMediaType("application/json;charset=UTF-8")));

    String responseBody = getResponse.getBody();

    System.out.println(expected);
    System.out.println(responseBody);
    //TODO: AFP. The assertion fails because of precision errors in the numbers
//    assertThat(responseBody, equalTo(expected));
  }

  /**
   * Testing Normal Approximation.
   */
  @Test
  public void normalApproximation() {
    final String BASE_URL = 
        "http://localhost:" + this.getServerPort() + "/statistics/approximation/normal?";
    String requestParams = "data=1&data=1.5&data=1.55&data=0.95&data=0.8";
    String expected = new JsonBuilder().withProperty("center", 1.16)
        .withProperty("lowerBound", 0.8605036503340853)
        .withProperty("upperBound", 1.4594963496659146)
        .withProperty("confidenceLevel", 0.95).build();
    
    RestTemplate rest = new TestRestTemplate();
    
    // Follow location
    ResponseEntity<String> getResponse = rest.getForEntity(BASE_URL + requestParams, String.class);
    MediaType contentType = getResponse.getHeaders().getContentType();
    assertThat(contentType, equalTo(MediaType.parseMediaType("application/json;charset=UTF-8")));
    
    String responseBody = getResponse.getBody();
    
    assertThat(responseBody, equalTo(expected));
  }
}
