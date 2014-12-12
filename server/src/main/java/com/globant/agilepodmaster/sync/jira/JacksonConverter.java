package com.globant.agilepodmaster.sync.jira;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;

/**
 * Converter to map custom Jira field proprietary names to our field names.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public class JacksonConverter extends MappingJackson2HttpMessageConverter {

  /**
   * Constructor.
   * 
   * @param customFieldsNaming custom object mapper.
   */
  public JacksonConverter(CustomFieldsNaming customFieldsNaming) {
    super();
    Assert.notNull(customFieldsNaming, "customFieldsNaming cannot be null");
    this.setObjectMapper(createObjectMapper(customFieldsNaming));

  }

  private ObjectMapper createObjectMapper(
      PropertyNamingStrategy propertyNamingStrategy) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setPropertyNamingStrategy(propertyNamingStrategy);
    return objectMapper;
  }
}
