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
public class Task extends SnapshotEntity {

  /**
   * Kind of statuses of Tasks.
   */
  public static enum Status {
    PENDING, INPROGRESS, CLOSED
  }

  /**
   * Kind of types of Tasks.
   */
  public static enum Type {
    USERSTORY, BUG, TASK
  }

  /**
   * Kind of priorities of Tasks.
   */
  public static enum Priority {
    LOW, MEDIUM, HIGH, CRITICAL
  }

  /**
   * Kind of severities of Tasks.
   */  
  public static enum Severity {
    LOW, MEDIUM, HIGH, CRITICAL
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
   * @param taskDTO task DTO.
   */
  public Task(Release release, Sprint sprint, Task parentTask) {
    this.release = release;
    this.sprint = sprint;
    this.parentTask = parentTask;
  }

  public boolean isAccepted() {
    return Status.CLOSED.equals(status);
  }
  
  public boolean isBug() {
    return Type.BUG.equals(type);
  }

  public boolean isOpen() {
    return !Status.CLOSED.equals(status);
  }

  @Transient
  public Project getProject() {
    return sprint != null? sprint.getRelease().getProject() : release.getProject();
  }
}
