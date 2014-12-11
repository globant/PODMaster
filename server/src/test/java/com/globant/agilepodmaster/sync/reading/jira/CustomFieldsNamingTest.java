package com.globant.agilepodmaster.sync.reading.jira;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.globant.agilepodmaster.sync.reading.jira.responses.IssuesSearchResult;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for CustomFieldsNaming class.
 * @author jose.dominguez@globant.com
 *
 */
public class CustomFieldsNamingTest {


  /**
   * Testing custom object Mapper.
   * 
   * @throws JsonParseException parsing exception
   * @throws JsonMappingException mapping exception
   * @throws IOException io exception
   */
  @Test
  public void testObjectMapper() throws JsonParseException,
      JsonMappingException, IOException {

    String json = "{\"issues\":[{\"id\":\"65230\",\"fields\":"
        + " {\"summary\":\"Kickoff\",\"customfield_10002\":2.0}}"
        + ",{\"id\":\"65227\",\"fields\":{\"summary\":"
        + " \"Make a widget Account.\"}}]}";

    Map<String, String> customFieldsMap = new HashMap<String, String>();
    customFieldsMap.put("storypoints", "customfield_10002");

    ObjectMapper mapper = new ObjectMapper();
    mapper.setPropertyNamingStrategy(new CustomFieldsNaming(
        customFieldsMap));
    IssuesSearchResult result = mapper
        .readValue(json, IssuesSearchResult.class);

    assertThat(result, notNullValue());
    assertThat(result.getIssues().get(0).getFields().getStorypoints(),
        equalTo(2.0f));
  }
}
