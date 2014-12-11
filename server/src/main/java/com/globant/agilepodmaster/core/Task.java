package com.globant.agilepodmaster.core;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A task can be a User Story, Bug or Task. It can belong to a Sprint or
 * Release. We consider that a task is part of a "backlog" when it belongs to a
 * Release.
 *
 * @author jose.dominguez@globant.com
 */
@Entity
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("PMD.TooManyFields")
public class Task extends SnapshotEntity {

  /**
   * Kind of statuses of Tasks.
   */
  public static enum Status {
    OPEN, INPROGRESS, REOPENED, RESOLVED, CLOSED, TODO, INREVIEW, DONE
  }

  /**
   * Kind of types of Tasks.
   */
  public static enum Type {
    BUG, EPIC, IMPROVEMENT, NEWFEATURE, STORY, TASK
  }

  /**
   * Kind of priorities of Tasks.
   */
  public static enum Priority {
    BLOCKER, CRITICAL, MAJOR, MINOR, TRIVIAL
  }

  /**
   * Kind of severities of Tasks.
   */  
  public static enum Severity {
    BLOCKER, CRITICAL, MAJOR, MINOR, TRIVIAL
  }

  /**
   * Kind of change that a task can have once the sprint associated has started.
   * This field is necessary for accuracy metric. We need to know if a task is
   * form the beginning of the sprint was removed or added.
   */
  public static enum ChangeDuringSprint {
    NOCHANGE, REMOVED, ADDED
  }

  @Getter
  @Setter(AccessLevel.PACKAGE)
  private String name;

  @Getter
  @Setter(AccessLevel.PACKAGE)
  private double effort;

  @Getter  
  @Setter(AccessLevel.PACKAGE)
  private int estimated;

  @Getter  
  @Setter(AccessLevel.PACKAGE)
  private int actual;

  @Getter  
  @Setter(AccessLevel.PACKAGE)
  private int remaining;

  @Getter  
  @Setter(AccessLevel.PACKAGE)
  private double accuracy;

  @Getter  
  @Setter(AccessLevel.PACKAGE)
  private Status status;

  @Getter  
  @Setter(AccessLevel.PACKAGE)
  private Severity severity;

  @Getter  
  @Setter(AccessLevel.PACKAGE)
  private Priority priority;

  @Getter  
  @Setter(AccessLevel.PACKAGE)
  private Type type;
  
  @Getter  
  @Setter(AccessLevel.PACKAGE)
  private ChangeDuringSprint changeDuringSprint;
  
  @Setter(AccessLevel.PACKAGE)
  private Date createdDate;

  @Getter
  @ManyToOne
  @JsonIgnore
  private Sprint sprint;

  @NonNull
  @ManyToOne
  @JsonIgnore
  @Getter
  private Release release;

  @OneToOne
  @JsonIgnore
  @Setter(AccessLevel.PACKAGE)
  @Getter
  private Task parentTask;
  
  @Setter
  @Getter
  @ManyToOne
  @JsonIgnore
  private PodMember owner;
  
  /**
   * Constructor.
   * 
   * @param release release of the task.
   * @param sprint sprint of the task.
   * @param parentTask parent task.
   */
  public Task(Release release, Sprint sprint, Task parentTask) {
    this.release = release;
    this.sprint = sprint;
    this.parentTask = parentTask;
    this.changeDuringSprint = ChangeDuringSprint.NOCHANGE;
  }

  /**
   * Tells if we can take the task for calculations.
   * @return true if we can take it, false otherwise.
   */
  public boolean isAccepted() {
    return Status.CLOSED.equals(status);
  }
  
  /**
   * Tells if the task is a bug.
   * @return true if it is a bug, false otherwise.
   */
  public boolean isBug() {
    return Type.BUG.equals(type);
  }

  /**
   * Tells if the task is open.
   * @return true if it is open, false otherwise.
   */
  public boolean isOpen() {
    return !Status.CLOSED.equals(status);
  }

  /**
   * Get the project of this task.
   * @return the project of this task.
   */
  @Transient
  public Project getProject() {
    return sprint != null ? sprint.getRelease().getProject() : release.getProject();
  }
}
