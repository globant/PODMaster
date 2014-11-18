package com.globant.agilepodmaster.sync.reading.jira.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Jira custom fields definition. 
 * @author jose.dominguez@globant.com
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFieldDefinition {

  private String id;
  private String name;
  private boolean custom;
  private boolean orderable;
  private boolean navigable;
  private boolean searchable;
  private Schema schema;

  /**
   * Jira custom fields schema.
   *
   */
  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Schema {
    private String type;
    private String schemaCustom;
    private int customId;
    private String system;
    private String items;
  }

}
