package com.globant.agilepodmaster.sync.reading.jira.responses;

import lombok.Data;

@Data
public class CustomFieldDefinition {

  public String id;
  public String name;
  public boolean custom;
  public boolean orderable;
  public boolean navigable;
  public boolean searchable;
  public Schema schema;

  public class Schema {
    public String type;
    public String custom;
    public int customId;
    public String system;
    public String items;
  }

}
