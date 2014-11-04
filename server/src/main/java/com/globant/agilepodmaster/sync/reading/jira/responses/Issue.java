package com.globant.agilepodmaster.sync.reading.jira.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Data;

/**
 * Jira issue.
 * @author jose.dominguez@globant.com
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
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
  @Data
  public class Fields {
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
    @Data
    public class Timetracking {
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
    @Data
    public class Issuetype {
      @SuppressWarnings({ "hiding" })
      private String self;

      @SuppressWarnings({ "hiding" })
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
    @Data
    public class Priority {
      @SuppressWarnings({ "hiding" })
      private String self;

      @SuppressWarnings({ "hiding" })
      private String id;

      private String iconUrl;

      private String name;
    }

    /**
     * Jira issue fields custom field..
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class CustomField {
      @SuppressWarnings({ "hiding" })
      private String id;

      private String value;
    }

    /**
     * Jira issue fields referenced field.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class ReferenceField {
      @SuppressWarnings({ "hiding" })
      private String id;

      private String name;
    }

    /**
     * Jira issue fields assigned.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class Assignee {
      @SuppressWarnings({ "hiding" })
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
      public class AvatarUrls {
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
    @Data
    public class Resolution {
      @SuppressWarnings({ "hiding" })
      private String self;

      @SuppressWarnings({ "hiding" })
      private String id;

      private String description;

      private String name;
    }

    /**
     * Jira issue fields status.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class Status {
      @SuppressWarnings({ "hiding" })
      private String self;

      private String description;

      private String iconUrl;

      private String name;

      @SuppressWarnings({ "hiding" })
      private String id;
    }

  }

}
