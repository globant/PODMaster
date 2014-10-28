package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
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
  
  @SuppressWarnings("unused")
  private Task() {
    // required by Hibernate
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