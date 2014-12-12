package com.globant.agilepodmaster.sync.jira;

import com.globant.agilepodmaster.sync.jira.responses.CustomFieldDefinition;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class to manage custom fields replacements. Jira manage custom fields
 * with its own names.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public class CustomFieldReplacements {

  private static final Map<String, String> customFieldsMap;
  static {
    Map<String, String> staticMap = new HashMap<String, String>();
    staticMap.put("Story Points", "storypoints");
    staticMap.put("Bug Severity", "severity");
    staticMap.put("Bug Priority", "bugpriority");
    staticMap.put("Bug Environment", "bugenvironment");
    customFieldsMap = Collections.unmodifiableMap(staticMap);
  }

  private CustomFieldDefinition[] customFields;

  /**
   * Constructor.
   * 
   * @param customFields custom field list obtained form JIra.
   */
  public CustomFieldReplacements(CustomFieldDefinition[] customFields) {
    Assert.notNull(customFields, "customFields cannot be null");
    this.customFields = Arrays.copyOf(customFields,customFields.length);
  }

  /**
   * Build a the map of replacements (name in Jira, name in our class) for
   * custom fields.
   * 
   * @return the map of replacements.
   */
  public Map<String, String> getCustomFieldReplacements() {
    Map<String, String> replacements = new HashMap<String, String>();
    for (Map.Entry<String, String> entry : this.customFieldsMap.entrySet()) {
      CustomFieldDefinition customField = findCustomFieldByName(entry.getKey());
      if (customField != null) {
        replacements.put(entry.getValue(), customField.getId());
      }

    }
    return replacements;
  }

  private CustomFieldDefinition findCustomFieldByName(String name) {
    for (CustomFieldDefinition customField : this.customFields) {
      if (customField.getName().equalsIgnoreCase(name)) {
        return customField;
      }
    }
    return null;

  }


}

