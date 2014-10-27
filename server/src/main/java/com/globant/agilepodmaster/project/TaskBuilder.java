package com.globant.agilepodmaster.project;

public interface TaskBuilder extends BacklogBuilder {
  TaskBuilder inSprint(int sprintId);
  TaskBuilder assignedTo(String string);
}
