package com.globant.agilepodmaster.sync.reading.jira.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Jira sprint report.
 * @author jose.dominguez@globant.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@SuppressWarnings("PMD")
public class SprintReport {

  private Contents contents;

  private Sprint sprint;

  /**
   * Jira sprint report contents.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  @Setter
  public static class Contents {


    /**
     * Constructor. Initializes lists.
     */
    public Contents() {
      completedIssues = new ArrayList<Issue>();
      incompletedIssues = new ArrayList<Issue>();
      puntedIssues = new ArrayList<Issue>();
      issueKeysAddedDuringSprint = new HashMap<String, Boolean>();
    }

    private List<Issue> completedIssues;

    private List<Issue> incompletedIssues;

    private List<Issue> puntedIssues;
    
    private java.util.Map<java.lang.String,java.lang.Boolean> issueKeysAddedDuringSprint;

  }

  /**
   * Jira sprint report sprint.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  @Setter
  public static class Sprint {

    private int id;

    private String name;

    private boolean closed;

    private String startDate;

    private String endDate;

    private String completeDate;

  }

  /**
   * Jira sprint report contents issue.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  @Setter
  public static class Issue {

    private int id;

    private String key;

    private Boolean hidden;

    private String typeName;

    private String typeId;

    private String summary;

    private String typeUrl;

    private boolean done;

    private String assignee;

    private String assigneeName;

    private String avatarUrl;

    private boolean hasCustomUserAvatar;

    private String color;    

    private EstimateStatistic estimateStatistic;

    private String statusId;

    private String statusName;

    private String statusUrl;

    private String priorityUrl;

    private String priorityName;

  }
  
  /**
   * Jira sprint report contents complete issue tracking statistic.
   */

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  @Setter
  public static class EstimateStatistic {

    private String statFieldId;

    private StatFieldValue statFieldValue;

  }
  
  /**
   * Jira sprint report contents complete issue tracking statistic statfield value.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  @Setter
  public static class StatFieldValue {

    private int value;

    private String text;

  }
  
}
