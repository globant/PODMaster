package com.globant.agilepodmaster.sync.reading.jira.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Data;

/**
 * Jira spring list.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SprintList {

  private List<SprintItem> sprints;

  private int rapidViewId;


  /**
   * Jira spring list item.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class SprintItem {
    private int id;
  
    private String name;
  
    private boolean closed;
  
    private int linkedPagesCount;
    
    private String state;
  
  }

}
