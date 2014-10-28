package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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

  public static enum Status {
    Pending, InProgress, Closed
  }

  public static enum Type {
    UserStory, Bug, Task
  }

  public static enum Priority {
    Low, Medium, High, Critical
  }

  public static enum Severity {
    Low, Medium, High, Critical
  }

  @NonNull
  public String name;

  public float effort;

  public float estimated;

  public float actual;

  public float remaining;

  public float accuracy;

  public Status taskStatus;

  public Severity severity;

  public Priority priority;

  public Type type;

  public Date createdDate;

  public Task parentTask;

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
