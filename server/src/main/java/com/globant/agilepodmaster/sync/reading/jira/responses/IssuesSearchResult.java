package com.globant.agilepodmaster.sync.reading.jira.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class IssuesSearchResult {
  
  public String expand;

  public int startAt;

  public int maxResults;

  public int total;

  public List<Issue> issues;

}