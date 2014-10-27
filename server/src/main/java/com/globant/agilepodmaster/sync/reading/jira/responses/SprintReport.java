package com.globant.agilepodmaster.sync.reading.jira.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class SprintReport {

  public Contents contents;

  public Sprint sprint;

  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public class Contents {

    public List<CompletedIssue> completedIssues;

    public List<IncompletedIssue> incompletedIssues;

    public List<PuntedIssue> puntedIssues;

    public CompletedIssuesEstimateSum completedIssuesEstimateSum;

    public IncompletedIssuesEstimateSum incompletedIssuesEstimateSum;

    public AllIssuesEstimateSum allIssuesEstimateSum;

    public PuntedIssuesEstimateSum puntedIssuesEstimateSum;
    
    public Object issueKeysAddedDuringSprint;

  }

  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public class Sprint {

    public int id;

    public String name;

    public boolean closed;

    public String startDate;

    public String endDate;

    public String completeDate;

  }

  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public static class CompletedIssue {

    public int id;

    public String key;

    public Boolean hidden;

    public String typeName;

    public String typeId;

    public String summary;

    public String typeUrl;

    public boolean done;

    public String assignee;

    public String assigneeName;

    public String avatarUrl;

    public boolean hasCustomUserAvatar;

    public String color;

    public TrackingStatistic trackingStatistic;

    public String statusId;

    public String statusName;

    public String statusUrl;

    public String priorityUrl;

    public String priorityName;

  }

  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public static class TrackingStatistic {

    public String statFieldId;

    public StatFieldValue statFieldValue;

  }

  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public static class StatFieldValue {

    public int value;

    public String text;

  }

  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public static class IncompletedIssue {

    public int id;

    public String key;

    public boolean hidden;

    public String typeName;

    public String typeId;

    public String summary;

    public String typeUrl;

    public boolean done;

    public String assignee;

    public String assigneeName;

    public String avatarUrl;

    public String color;

    public TrackingStatistic2 trackingStatistic;

    public String statusId;

    public String statusName;

    public String statusUrl;

  }

  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public static class StatFieldValue2 {

    public int value;

    public String text;

  }

  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public static class TrackingStatistic2 {

    public String statFieldId;

    public StatFieldValue2 statFieldValue;

  }

  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public static class StatFieldValue3 {

    public int value;

    public String text;

  }
  
  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public static class TrackingStatistic3 {

    public String statFieldId;

    public StatFieldValue3 statFieldValue;

  }
  
  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public static class PuntedIssue {

    public int id;

    public String key;

    public boolean hidden;

    public String typeName;

    public String typeId;

    public String summary;

    public String typeUrl;

    public boolean done;

    public String assignee;

    public String assigneeName;

    public String avatarUrl;

    public String color;

    public TrackingStatistic3 trackingStatistic;

    public String statusId;

    public String statusName;

    public String statusUrl;

  }
  
  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public static class CompletedIssuesEstimateSum {

    public int value;

    public String text;

  }
  
  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public static class IncompletedIssuesEstimateSum {

    public int value;

    public String text;

  }

  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public static class AllIssuesEstimateSum {

    public int value;

    public String text;

  }

  @JsonIgnoreProperties(ignoreUnknown=true)
  @Data
  public static class PuntedIssuesEstimateSum {

    public int value;

    public String text;

  }

}
