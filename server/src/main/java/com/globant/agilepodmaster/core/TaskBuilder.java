package com.globant.agilepodmaster.core;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Getter;

import com.globant.agilepodmaster.sync.AbstractBuilder;
import com.globant.agilepodmaster.sync.BacklogBuilder;
import com.globant.agilepodmaster.sync.SnapshotBuilder;
import com.globant.agilepodmaster.sync.SnapshotDataCollector;

public class TaskBuilder<P, T extends SnapshotDataCollector> extends AbstractBuilder<P, T> {

  @Getter(AccessLevel.PROTECTED)
  private final Task task;

  @Getter(AccessLevel.PROTECTED)
  private String owner;

  @Getter(AccessLevel.PROTECTED)
  private Release release;

  @Getter(AccessLevel.PROTECTED)
  private Sprint sprint;

  protected TaskBuilder(Task parent, Release release, Sprint sprint, P parentBuilder) {
    super(parentBuilder);
    this.task = new Task(release, sprint, parent);
    this.release = release;
    this.sprint = sprint;
  }

  /**
   * Setter for name field.
   * 
   * @param name
   *          the name.
   * @return the same Builder.
   */
  public TaskBuilder<P, T> name(String name) {
    task.setName(name);
    return this;
  }

  /**
   * Setter for owner field.
   * 
   * @param owner
   *          the owner.
   * @return the same Builder.
   */
  public TaskBuilder<P, T> owner(String owner) {
    this.owner = owner;
    return this;
  }

  /**
   * Setter for effort field.
   * 
   * @param effort
   *          the effort.
   * @return the same Builder.
   */
  public TaskBuilder<P, T> effort(float effort) {
    task.setEffort(effort);
    return this;
  }

  /**
   * Setter for estimated field.
   * 
   * @param estimated
   *          the estimated.
   * @return the same Builder.
   */
  public TaskBuilder<P, T> estimated(int estimated) {
    task.setEstimated(estimated);
    return this;
  }

  /**
   * Setter for remaining field.
   * 
   * @param remaining
   *          the remaining.
   * @return the same Builder.
   */
  public TaskBuilder<P, T> remaining(int remaining) {
    task.setRemaining(remaining);
    return this;
  }

  /**
   * Setter for actual field.
   * 
   * @param actual
   *          the actual.
   * @return the same Builder.
   */
  public TaskBuilder<P, T> actual(Integer actual) {
    task.setActual((actual != null) ? actual.intValue() : 0);
    return this;

  }

  /**
   * Setter for createDate field.
   * 
   * @param createdDate
   *          the createDate.
   * @return the same Builder.
   */
  public TaskBuilder<P, T> createDate(Date createdDate) {
    task.setCreatedDate(createdDate);
    return this;

  }

  /**
   * Setter for type field.
   * 
   * @param type
   *          the type.
   * @return the same Builder.
   */

  public TaskBuilder<P, T> type(String type) {
    String value = type != null ? type.toUpperCase().replaceAll("\\s","") : null;
    task.setType(Task.Type.valueOf(value));    
    return this;

  }

  /**
   * Setter for status field.
   * 
   * @param status
   *          the status.
   * @return the same Builder.
   */
  public TaskBuilder<P, T> status(String status) {
    String value = status != null ? status.toUpperCase().replaceAll("\\s","") : null;
    task.setStatus(Task.Status.valueOf(value));
    return this;

  }

  /**
   * Setter for severity field.
   * 
   * @param severity
   *          the severity.
   * @return the same Builder.
   */
  public TaskBuilder<P, T> severity(String severity) {
    String value = severity != null ? severity.toUpperCase().replaceAll("\\s","") : null;
    task.setSeverity(Task.Severity.valueOf(value));
    return this;

  }

  /**
   * Setter for priority field.
   * 
   * @param priority
   *          the priority.
   * @return the same Builder.
   */
  public TaskBuilder<P, T> priority(String priority) {
    String value = priority != null ? priority.toUpperCase().replaceAll("\\s","") : null;
    task.setPriority(Task.Priority.valueOf(value));
    return this;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public  TaskBuilder<TaskBuilder, TaskBuilder> addSubTask() {
    TaskBuilder<TaskBuilder, TaskBuilder> nestedBuilder = 
        subTaskBuilder(this.task, release, sprint, this);       
    this.addNestedBuilder((T) nestedBuilder);
    return nestedBuilder;
  }
  
  @SuppressWarnings("rawtypes")
  public TaskBuilder addToTask() {
    return (TaskBuilder)this.getParentBuilder();
  }
  
  @SuppressWarnings("rawtypes")
  public BacklogBuilder addToSprint() {
    P parentBuilder = this.getParentBuilder();
    return parentBuilder instanceof BacklogBuilder
        ? (BacklogBuilder) parentBuilder
        : (BacklogBuilder) ((TaskBuilder) parentBuilder).getParentBuilder();
  }
  
  @Override
  protected void doCollect(SnapshotBuilder snapshotBuilder) {
    snapshotBuilder.addTaskAssignedTo(owner, task);
  }

  public static TaskBuilder<BacklogBuilder, TaskBuilder<?,?>> taskBuilder(
      Release release, Sprint sprint, BacklogBuilder backlogBuilder) {
    return new TaskBuilder<BacklogBuilder, TaskBuilder<?,?>>(null, release, sprint,
        backlogBuilder);
  }

  @SuppressWarnings("rawtypes")
  public static TaskBuilder<TaskBuilder, TaskBuilder> subTaskBuilder(Task task,
      Release release, Sprint sprint, TaskBuilder<?,?> taskBuilder) {
    return new TaskBuilder<TaskBuilder, TaskBuilder>(task, release, sprint,
        taskBuilder);
  }
}
