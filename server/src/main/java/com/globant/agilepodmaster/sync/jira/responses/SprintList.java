package com.globant.agilepodmaster.sync.jira.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Jira spring list.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SprintList {

  private List<SprintItem> sprints;

  private int rapidViewId;


  /**
   * Jira spring list item.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  @Setter
  public static class SprintItem {
    private int id;
  
    private String name;
  
    private boolean closed; 
  
    private int linkedPagesCount;
    
    private String state;
  
  }

}
