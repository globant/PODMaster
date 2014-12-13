package com.globant.agilepodmaster.metrics;

import static com.globant.agilepodmaster.ssl.SSLTestHelper.createNotVerifyingHttpRequestFactory;
import static com.globant.agilepodmaster.ssl.SSLTestHelper.restoreMemento;
import static com.globant.agilepodmaster.ssl.SSLTestHelper.trustAllCertificates;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

import java.io.IOException;
import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import com.globant.agilepodmaster.AbstractIntegrationTest;
import com.globant.agilepodmaster.core.IDummyDataGenerator;
import com.globant.agilepodmaster.core.Snapshot;
import com.globant.agilepodmaster.core.SnapshotRepository;
import com.globant.agilepodmaster.ssl.SSLTestHelper.Memento;

/**
 * Test for MetricsController.
 * @author Andres Postiglioni.
 *
 */
public class MetricsControllerTest extends AbstractIntegrationTest {
  private static Memento sslMemento;

  @Autowired
  private SnapshotRepository repo;

  private Snapshot snapshot;

  @BeforeClass
  public static void beforeClass() {
    sslMemento = trustAllCertificates();
  }

  @AfterClass
  public static void afterClass() {
    restoreMemento(sslMemento);
  }
  
  /**
   * Creates data for testing in the DB.
   */
  @Before
  public void createData() {
    snapshot = repo.save(new IDummyDataGenerator().buildScenario1());
  }

  /**
   * Test json returned by controller schema.
   * @throws IOException exception that is thrown when the file cannot be found.
   * @throws ProcessingException exception that is thrown when validation has an issue. 
   */
  @Test
  public void test() throws IOException, ProcessingException {
    String schemaResource = 
        "/com/globant/agilepodmaster/metrics/metrics-collection-resource.schema.json";
    String baseUrl = super.buildServerUrl(
        String.format("/snapshots/%d/metrics?aggregation=pod", snapshot.getId())
    );

    JsonValidator validator = JsonSchemaFactory.byDefault().getValidator();

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    HttpEntity<String> requestEntity = new HttpEntity<String>("parameters", headers);
    
    ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();

    resource.setAccessTokenUri("https://localhost:9080/oauth/token");
    resource.setClientId("clientapp");
    resource.setClientSecret("123456");
    resource.setId("restservice");
    resource.setUsername("roy");
    resource.setPassword("spring");

    OAuth2RestTemplate rest = new OAuth2RestTemplate(resource);
    rest.setRequestFactory(createNotVerifyingHttpRequestFactory());
    
    ResponseEntity<String> responseEntity = rest.exchange(
        baseUrl, HttpMethod.GET, requestEntity, String.class);
    
    String response = responseEntity.getBody();
    System.out.println(response);

    JsonNode json = JsonLoader.fromString(response);
    JsonNode schema = JsonLoader.fromResource(schemaResource);
    
    ProcessingReport validationReport = validator.validate(schema, json);
    assertThat(validationReport.isSuccess(), equalTo(true));
  }
}
