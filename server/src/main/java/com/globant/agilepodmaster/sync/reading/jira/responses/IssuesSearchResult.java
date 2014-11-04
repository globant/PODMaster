package com.globant.agilepodmaster.sync.reading.jira.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Data;

/**
 * Jira issues search result.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class IssuesSearchResult {
  
  private String expand;

  private int startAt;

  private int maxResults;

  private int total;

  private List<Issue> issues;

}