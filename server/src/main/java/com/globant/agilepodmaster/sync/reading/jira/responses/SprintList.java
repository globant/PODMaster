package com.globant.agilepodmaster.sync.reading.jira.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class SprintList {

  public List<SprintItem> sprints;

  public int rapidViewId;


  
  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public static class SprintItem {
    public int id;
  
    public String name;
  
    public boolean closed;
  
    public int linkedPagesCount;
    
    public String state;
  
  }

}
