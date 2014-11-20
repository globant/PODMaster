package com.globant.agilepodmaster.sync;

import java.util.Date;

import com.globant.agilepodmaster.core.TaskBuilder;
import com.globant.agilepodmaster.core.Release;
import com.globant.agilepodmaster.core.Sprint;

@SuppressWarnings("rawtypes")
public class BacklogBuilder extends AbstractBuilder<ReleaseBuilder, TaskBuilder> {
  private Sprint sprint;
  private Release release;
  public BacklogBuilder(Release release, String sprintName, Date startDate, Date endDate,
      ReleaseBuilder releaseBuilder) {
    super(releaseBuilder);
    this.release = release;
    this.sprint = new Sprint(sprintName, release, startDate, endDate);
  }

  public BacklogBuilder(Release release, ReleaseBuilder releaseBuilder) {
    super(releaseBuilder);
    this.release = release;
  }

  public TaskBuilder withTask() {
    TaskBuilder nestedBuilder = TaskBuilder.taskBuilder(release, sprint, this);
    this.addNestedBuilder(nestedBuilder);
    return nestedBuilder;
  }

  public ReleaseBuilder addToRelease() {
    return super.getParentBuilder();
  }

  @Override
  protected void doCollect(SnapshotBuilder snapshotBuilder) {
    if (sprint != null) {
      snapshotBuilder.addSprint(sprint);
    }
  }
}
