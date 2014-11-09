package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
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
    UserStory, Bug, Task
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
  private String name;

  private double effort;

  private double estimated;

  private double actual;

  private double remaining;

  private double accuracy;

  private Status status;

  private Severity severity;

  private Priority priority;

  private Type type;

  private Date createdDate;

  @OneToOne
  @JsonIgnore
  private Task parentTask;

  @ManyToOne
  @JsonIgnore
  private Sprint sprint;

  @NonNull
  @ManyToOne
  @JsonIgnore
  private Release release;

  @ManyToOne
  @JsonIgnore
  private PodMember owner;

}
