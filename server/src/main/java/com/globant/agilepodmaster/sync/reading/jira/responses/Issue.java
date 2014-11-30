package com.globant.agilepodmaster.sync.reading.jira.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.globant.agilepodmaster.core.Sprint;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Jira issue.
 * @author jose.dominguez@globant.com
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@SuppressWarnings("PMD")
public class Issue {
  
  private String expand;

  private String id;

  private String self;

  private String key;

  private Fields fields;
  

  /**
   * Jira issue fields.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  @Setter
  @SuppressWarnings("PMD")
  public static class Fields {
    private String summary;

    private Timetracking timetracking;

    private Issuetype issuetype;

    private Priority priority;

    private Status status;

    private String created;

    private Assignee assignee;

    private Resolution resolution;

    private List<ReferenceField> components;

    private String resolutiondate;

    private Issue parent;

    private List<String> labels;

    // region custom fields

    private CustomField severity;

    private float storypoints;

    private CustomField bugpriority;

    private CustomField bugenvironment;

    // endregion

    /**
     * Jira issue fields Timetracking.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Timetracking {
      private String originalEstimate;

      private String remainingEstimate;

      private int originalEstimateSeconds;

      private int remainingEstimateSeconds;

      private String timeSpent;

      private Integer timeSpentSeconds;
    }

    /**
     * Jira issue fields Issuetype.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Issuetype {
      private String self;

      private String id;

      private String description;

      private String iconUrl;

      private String name;

      private Boolean subtask;
    }

    /**
     * Jira issue fields priority.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Priority {
      private String self;

      private String id;

      private String iconUrl;

      private String name;
    }

    /**
     * Jira issue fields custom field..
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class CustomField {
      private String id;

      private String value;
    }

    /**
     * Jira issue fields referenced field.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class ReferenceField {
      private String id;

      private String name;
    }

    /**
     * Jira issue fields assigned.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Assignee {
      private String self;

      private String name;

      private String emailAddress;

      private AvatarUrls avatarUrls;

      private String displayName;

      private Boolean active;

      /**
       * Jira issue fields assigned AvatarUrls.
       */
      @JsonIgnoreProperties(ignoreUnknown = true)
      @Data
      public static class AvatarUrls {
        @SuppressWarnings("checkstyle:membername")
        private String __invalid_name__16x16;
        @SuppressWarnings("checkstyle:membername")
        private String __invalid_name__48x48;
      }
    }

    /**
     * Jira issue fields reslution.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Resolution {
      private String self;

      private String id;

      private String description;

      private String name;
    }

    /**
     * Jira issue fields status.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Status {
      private String self;

      private String description;

      private String iconUrl;

      private String name;

      private String id;
    }

  }

}
