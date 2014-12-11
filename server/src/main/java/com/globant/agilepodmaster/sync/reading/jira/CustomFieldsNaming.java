package com.globant.agilepodmaster.sync.reading.jira;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

import java.util.Map;


/**
 * This class defines a mapper namming strategy to map json custom names to our
 * field names.
 * 
 * @author jose.dominguez@globant.com
 *
 */
class CustomFieldsNaming extends PropertyNamingStrategy {

  Map<String, String> customFieldsMap;

  /**
   * Constructor.
   * 
   * @param customFieldsMap map with custom fields to rename.
   */
  public CustomFieldsNaming(Map<String, String> customFieldsMap) {
    this.customFieldsMap = customFieldsMap;
  }

  @Override
  public String nameForSetterMethod(MapperConfig<?> config,
      AnnotatedMethod method, String defaultName) {

    return customFieldsMap.get(defaultName);
  }
}
