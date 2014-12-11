package com.globant.agilepodmaster.sync.reading.jira.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Jira issues search result.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class IssuesSearchResult {
  
  private String expand;

  private int startAt;

  private int maxResults;

  private int total;

  private List<Issue> issues;

}