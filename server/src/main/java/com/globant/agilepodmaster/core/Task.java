package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.globant.agilepodmaster.sync.reading.TaskDTO;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * A task can be a User Story, Bug or Task. It can belong to a Sprint or
 * Release. We consider that a task is part of a "backlog" when it belongs to a
 * Release.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Task extends AbstractEntity {

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

  @NonNull
  @Getter
  private String name;

  @Getter
  private double effort;

  @Getter  
  private int estimated;

  @Getter  
  private int actual;

  @Getter  
  private int remaining;

  @Getter  
  private double accuracy;

  @Getter  
  private Status status;

  @Getter  
  private Severity severity;

  @Getter  
  private Priority priority;

  @Getter  
  private Type type;
  
  private Date createdDate;

  @Getter
  @ManyToOne
  @JsonIgnore
  private Sprint sprint;

  @NonNull
  @ManyToOne
  @JsonIgnore
  private Release release;

  @OneToOne
  @JsonIgnore
  @Setter
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
  public Task(Release release, Sprint sprint, Task parentTask, TaskDTO taskDTO) {
    this.release = release;
    this.sprint = sprint;
    this.parentTask = parentTask;
    this.name = taskDTO.getName();
    this.createdDate = taskDTO.getCreateDate();
    this.actual = taskDTO.getActual();
    this.effort = taskDTO.getEffort();
    this.estimated = taskDTO.getEstimated();
    this.remaining = taskDTO.getRemaining();
    this.priority = Task.Priority.valueOf(taskDTO.getPriority().name());
    this.severity = Task.Severity.valueOf(taskDTO.getSeverity().name());
    this.status = Task.Status.valueOf(taskDTO.getStatus().name());
    this.type = Task.Type.valueOf(taskDTO.getType().name());
  }

  public boolean isAccepted() {
    return Status.Closed.equals(status);
  }
}
