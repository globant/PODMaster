package com.globant.agilepodmaster.sync.reading.jira.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Issue {

  public String expand;

  public String id;

  public String self;

  public String key;

  public Fields fields;

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public class Fields {
    public String summary;

    public Timetracking timetracking;

    public Issuetype issuetype;

    public Priority priority;

    public Status status;

    public String created;

    public Assignee assignee;

    public Resolution resolution;

    public List<ReferenceField> components;

    public String resolutiondate;

    public Issue parent;

    public List<String> labels;

    // region custom fields

    public CustomField severity;

    public float storypoints;

    public CustomField bugpriority;

    public CustomField bugenvironment;

    // endregion

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class Timetracking {
      public String originalEstimate;

      public String remainingEstimate;

      public int originalEstimateSeconds;

      public int remainingEstimateSeconds;

      public String timeSpent;

      public Integer timeSpentSeconds;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class Issuetype {
      public String self;

      public String id;

      public String description;

      public String iconUrl;

      public String name;

      public Boolean subtask;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class Priority {
      public String self;

      public String id;

      public String iconUrl;

      public String name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class CustomField {
      public String id;

      public String value;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class ReferenceField {
      public String id;

      public String name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class Assignee {
      public String self;

      public String name;

      public String emailAddress;

      public AvatarUrls avatarUrls;

      public String displayName;

      public Boolean active;

      @JsonIgnoreProperties(ignoreUnknown = true)
      @Data
      public class AvatarUrls {
        @SuppressWarnings("checkstyle:membername")
        public String __invalid_name__16x16;
        @SuppressWarnings("checkstyle:membername")
        public String __invalid_name__48x48;
      }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class Resolution {
      public String self;

      public String id;

      public String description;

      public String name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class Status {
      public String self;

      public String description;

      public String iconUrl;

      public String name;

      public String id;
    }

  }

}
