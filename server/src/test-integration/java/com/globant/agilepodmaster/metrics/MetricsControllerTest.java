package com.globant.agilepodmaster.metrics;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

public class MetricsControllerTest extends AbstractIntegrationTest {
  @Autowired
  private SnapshotRepository repo;

  private Snapshot snapshot;

  @Before
  public void createData() {
    snapshot = repo.save(new IDummyDataGenerator().buildScenario1());
  }

  @Test
  public void test() throws IOException, ProcessingException {
    String SCHEMA = "/com/globant/agilepodmaster/metrics/metrics-collection-resource.schema.json";
    String BASE_URL = "http://localhost:" + this.getServerPort() 
        + "/snapshots/" + snapshot.getId() +
        "/metrics?aggregation=pod";

    JsonValidator validator = JsonSchemaFactory.byDefault().getValidator();

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    HttpEntity<String> requestEntity = new HttpEntity<String>("parameters", headers);
    
    RestTemplate rest = new TestRestTemplate();
    ResponseEntity<String> responseEntity = rest.exchange(
        BASE_URL, HttpMethod.GET, requestEntity, String.class);
    
    String response = responseEntity.getBody();
    System.out.println(response);

    JsonNode json = JsonLoader.fromString(response);
    JsonNode schema = JsonLoader.fromResource(SCHEMA);
    
    ProcessingReport validationReport = validator.validate(schema, json);
    assertThat(validationReport.isSuccess(), equalTo(true));
  }
}
