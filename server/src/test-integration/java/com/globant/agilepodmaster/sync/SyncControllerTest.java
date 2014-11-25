package com.globant.agilepodmaster.sync;


import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import com.globant.agilepodmaster.AbstractIntegrationTest;
import com.globant.agilepodmaster.core.SnapshotRepository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

/**
 * Test for SyncController.
 * @author jose.dominguez@globant.com
 *
 */
public class SyncControllerTest extends AbstractIntegrationTest {
  @Autowired
  private SnapshotRepository repo;
  
  /**
   * Test if the controller returns the right json.
   * @throws IOException exception that is thrown if we can read file.
   * @throws ProcessingException 
   */
  @Test
  public void testShouldReturnCorrectJsonSchema() throws IOException, ProcessingException {
    String schemaResource = "/com/globant/agilepodmaster/sync/sync-response-resource.schema.json";
    String baseUrl = "http://localhost:" + this.getServerPort() + "/sync";

    JsonValidator validator = JsonSchemaFactory.byDefault().getValidator();

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    HttpEntity<String> requestEntity = new HttpEntity<String>("parameters", headers);
    
    RestTemplate rest = new TestRestTemplate();
    ResponseEntity<String> responseEntity = rest.exchange(
        baseUrl, HttpMethod.POST, requestEntity, String.class);
    
    String response = responseEntity.getBody();
    System.out.println(response);

    JsonNode json = JsonLoader.fromString(response);
    JsonNode schema = JsonLoader.fromResource(schemaResource);
    
    ProcessingReport validationReport = validator.validate(schema, json);
    assertThat(validationReport.isSuccess(), equalTo(true));
  }
  
}
