package com.globant.agilepodmaster.sync.reading;

import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.reading.jira.DateUtil;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


/**
 * DTO that represents a task taken from a data source.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@EqualsAndHashCode(exclude = { "createDate" })
public class TaskDTO implements Serializable {
  
  /**
   * Kind of statuses of Tasks.
   */
  public static enum Status {
    Pending, InProgress, Closed
  }

  /**
   * Kind of types of Tasks.
   */
  public static enum Type {
    Story, Bug, Task
  }

  /**
   * Kind of priorities of Tasks.
   */
  public static enum Priority {
    Low, Medium, High, Critical
  }

  /**
   * Kind of severities of Tasks.
   */  
  public static enum Severity {
    Low, Medium, High, Critical
  }
  
  @Getter
  private String name;
  @Getter
  private String owner;
  @Getter
  private float effort;
  @Getter
  private int estimated;
  @Getter
  private int remaining;
  @Getter
  private int actual;
  @Getter
  private Status status;
  @Getter
  private Type type;
  @Getter
  private Priority priority;
  @Getter
  private Severity severity;

  @Getter
  @Setter
  private List<TaskDTO> subTasks;
  
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE) 
  private Date createDate;
  
  /**
   * Constructor.
   * @param builder builder used to create a new TaskDTO.
   */
  public TaskDTO(Builder builder) {
    subTasks = new ArrayList<TaskDTO>();
    name = builder.name;
    owner = builder.owner;
    effort = builder.effort;
    estimated = builder.estimated;
    remaining = builder.remaining;
    actual = builder.actual;
    status = builder.status;
    type = builder.type;
    priority = builder.priority;
    severity = builder.severity;
    createDate = builder.createDate;
  }

  /**
   * createDate getter.
   * @return the date.
   */
  public Date getCreateDate() {
    if (createDate == null) {
      return null;
    }
    return new Date(createDate.getTime());
  }

  /**
   * createDate setter.
   * @param createDate the date.
   */
  public void setCreateDate(Date createDate) {
    Assert.notNull(createDate, "createDate is null");
    this.createDate = new Date(createDate.getTime());
  }
  
  /**
   * Builder to build TaskDTOs.
   * @author jose.dominguez@globant.com
   *
   */
  public static class Builder {
    
    private String name;
    
    private String owner;

    private float effort;

    private int estimated;

    private int remaining;

    private int actual;
    
    private Status status = TaskDTO.Status.Pending;

    private Type type = TaskDTO.Type.Task;
    
    private Priority priority = TaskDTO.Priority.Medium;
    
    private Severity severity = TaskDTO.Severity.Medium;
    
    private Date createDate;
    
    private SyncContext context;
    
    /**
     * Constructor.
     * @param context context to log.
     */
    public Builder(SyncContext context) {
      this.context = context;
    }
    
    /**
     * Setter for name field.
     * @param name the name.
     * @return the same Builder.
     */
    public Builder name(String name) {
      this.name = truncate(name, 100);
      return this;
    }
    
    /**
     * Setter for owner field.
     * @param owner the owner.
     * @return the same Builder.
     */
    public Builder owner(String owner) {
      this.owner = owner;
      return this;
    }
    
    /**
     * Setter for effort field.
     * @param effort the effort.
     * @return the same Builder.
     */
    public Builder effort(float effort) {
      this.effort = effort;
      return this;
    }
    
    /**
     * Setter for estimated field.
     * @param estimated the estimated.
     * @return the same Builder.
     */
    public Builder estimated(int estimated) {
      this.estimated = estimated;    
      return this;
    }
    
    /**
     * Setter for remaining field.
     * @param remaining the remaining.
     * @return the same Builder.
     */
    public Builder remaining(int remaining) {
      this.remaining = remaining;      
      return this;
    }
    
    /**
     * Setter for actual field.
     * @param actual the actual.
     * @return the same Builder.
     */
    public Builder actual(Integer actual) {
      this.actual = (actual != null) ? actual.intValue() : 0;
      return this;
    }
    
    /**
     * Setter for createDate field.
     * @param createdIssueDate the createDate.
     * @return the same Builder.
     */
    public Builder createDate(String createdIssueDate) {
      if (createdIssueDate != null) {
        Date createDate = DateUtil.getDate(createdIssueDate, context);
        if (createDate != null) {
          this.createDate = createDate;
        }
      }
      return this;
    }
    
    /**
     * Setter for type field.
     * @param issueType the type.
     * @return the same Builder.
     */
    
    public Builder type(String issueType) {
      if (issueType != null) {
        try {
          this.type = TaskDTO.Type.valueOf(issueType);
        } catch (IllegalArgumentException e) {
          context.error("Maping type value: " + issueType);
        }
      }
      return this;
    }
    
    /**
     * Setter for status field.
     * @param issueStatus the status.
     * @return the same Builder.
     */
    public Builder status(String issueStatus) {
      if (issueStatus != null) {
        try {

          status = TaskDTO.Status.valueOf(issueStatus);
        } catch (IllegalArgumentException e) {
          context.error("Maping status value: " + issueStatus);
        }
      }
      return this;
    }

    /**
     * Setter for severity field.
     * @param issueSeverity the severity.
     * @return the same Builder.
     */
    public Builder severity(String issueSeverity) {
      if (issueSeverity != null) {
        try {

          this.severity = TaskDTO.Severity.valueOf(issueSeverity);
        } catch (IllegalArgumentException e) {
          context.error("Maping severity value: " + issueSeverity);
        }
      }
      return this;
    }
    
    /**
     * Setter for priority field.
     * @param issuePriority the priority.
     * @return the same Builder.
     */
    public Builder priority(String issuePriority) {
      if (issuePriority != null) {
        try {
          this.priority = TaskDTO.Priority.valueOf(issuePriority);
        } catch (IllegalArgumentException e) {
          context.error("Maping priority value: " + issuePriority);
        }
      }
      return this;
    }
    
    /**
     * Builds DTO with the fields set.
     * @return a new TaskDTO.
     */
    public TaskDTO build() {
      return new TaskDTO(this);
    }
    
    private String truncate(String inputString, int maxChar) {
      int maxLength = (inputString.length() < maxChar) ? inputString.length()
          : maxChar;
      return inputString.substring(0, maxLength);
    }
    
    
  }
  
 
  
}   
