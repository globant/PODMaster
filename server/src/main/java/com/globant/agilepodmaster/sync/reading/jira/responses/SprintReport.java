package com.globant.agilepodmaster.sync.reading.jira.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Data;

/**
 * Jira sprint report.
 * @author jose.dominguez@globant.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuppressWarnings("PMD")
public class SprintReport {

  private Contents contents;

  private Sprint sprint;

  /**
   * Jira sprint report contents.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class Contents {

    private List<CompletedIssue> completedIssues;

    private List<IncompletedIssue> incompletedIssues;

    private List<PuntedIssue> puntedIssues;

    private CompletedIssuesEstimateSum completedIssuesEstimateSum;

    private IncompletedIssuesEstimateSum incompletedIssuesEstimateSum;

    private AllIssuesEstimateSum allIssuesEstimateSum;

    private PuntedIssuesEstimateSum puntedIssuesEstimateSum;
    
    private Object issueKeysAddedDuringSprint;

  }

  /**
   * Jira sprint report sprint.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class Sprint {

    private int id;

    private String name;

    private boolean closed;

    private String startDate;

    private String endDate;

    private String completeDate;

  }

  /**
   * Jira sprint report contents complete issue.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class CompletedIssue {

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

    private TrackingStatistic trackingStatistic;

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
  @Data
  public static class TrackingStatistic {

    private String statFieldId;

    private StatFieldValue statFieldValue;

  }
  
  /**
   * Jira sprint report contents complete issue tracking statistic statfield value.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class StatFieldValue {

    private int value;

    private String text;

  }

  /**
   * Jira sprint report contents imcomplete issue.
   */  
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class IncompletedIssue {

    private int id;

    private String key;

    private boolean hidden;

    private String typeName;

    private String typeId;

    private String summary;

    private String typeUrl;

    private boolean done;

    private String assignee;

    private String assigneeName;

    private String avatarUrl;

    private String color;

    private TrackingStatistic2 trackingStatistic;

    private String statusId;

    private String statusName;

    private String statusUrl;

  }

  /**
   * Jira sprint report contents TrackingStatistic2 StatFieldValue2.
   */ 
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class StatFieldValue2 {

    private int value;

    private String text;

  }

  /**
   * Jira sprint report contents TrackingStatistic2.
   */   
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class TrackingStatistic2 {

    private String statFieldId;

    private StatFieldValue2 statFieldValue;

  }

  /**
   * Jira sprint report contents TrackingStatistic3 StatFieldValue3.
   */ 
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class StatFieldValue3 {

    private int value;

    private String text;

  }
  
  /**
   * Jira sprint report contents TrackingStatistic3.
   */   
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class TrackingStatistic3 {

    private String statFieldId;

    private StatFieldValue3 statFieldValue;

  }
  
  /**
   * Jira sprint report contents PuntedIssue.
   */ 
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class PuntedIssue {

    private int id;

    private String key;

    private boolean hidden;

    private String typeName;

    private String typeId;

    private String summary;

    private String typeUrl;

    private boolean done;

    private String assignee;

    private String assigneeName;

    private String avatarUrl;

    private String color;

    private TrackingStatistic3 trackingStatistic;

    private String statusId;

    private String statusName;

    private String statusUrl;

  }
  
  /**
   * Jira sprint report contents CompletedIssuesEstimateSum.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class CompletedIssuesEstimateSum {

    private int value;

    private String text;

  }
  
  /**
   * Jira sprint report contents IncompletedIssuesEstimateSum.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class IncompletedIssuesEstimateSum {

    private int value;

    private String text;

  }
  
  /**
   * Jira sprint report contents AllIssuesEstimateSum.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class AllIssuesEstimateSum {

    private int value;

    private String text;

  }

  /**
   * Jira sprint report contents PuntedIssuesEstimateSum.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class PuntedIssuesEstimateSum {

    private int value;

    private String text;

  }

}
