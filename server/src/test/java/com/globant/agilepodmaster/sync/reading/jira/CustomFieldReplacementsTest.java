package com.globant.agilepodmaster.sync.reading.jira;

import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;

import com.globant.agilepodmaster.readers.releases.CustomFieldReplacements;
import com.globant.agilepodmaster.readers.releases.jira.responses.CustomFieldDefinition;

import org.junit.Test;

import java.util.Map;

/**
 * Test for CustomFieldReplacements class.
 * @author jose.dominguez@globant.com
 *
 */
public class CustomFieldReplacementsTest {

  /**
   * Test for method that return replacements.
   */
  @Test
  public void testCustomFieldReplacements() {

    CustomFieldDefinition customField1 = new CustomFieldDefinition();
    customField1.setId("customfield_10002");
    customField1.setName("Story Points");
    CustomFieldDefinition customField2 = new CustomFieldDefinition();
    customField2.setId("customfield_10615");
    customField2.setName("Realized Date");
    CustomFieldDefinition customField3 = new CustomFieldDefinition();
    customField3.setId("customfield_10106");
    customField3.setName("Bug Environment");
    CustomFieldDefinition customField4 = new CustomFieldDefinition();
    customField4.setId("summary");
    customField4.setName("Summary");

    CustomFieldDefinition[] customFields = {
        customField1, customField2, customField3, customField4};

    CustomFieldReplacements customFieldReplacements = new CustomFieldReplacements(
        customFields);

    Map<String, String> result = customFieldReplacements
        .getCustomFieldReplacements();

    assertThat(result, hasEntry("storypoints", "customfield_10002"));
    assertThat(result, hasEntry("bugenvironment", "customfield_10106"));


  }
}
