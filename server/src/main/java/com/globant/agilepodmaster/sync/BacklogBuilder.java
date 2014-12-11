package com.globant.agilepodmaster.sync;

import java.util.Date;

import com.globant.agilepodmaster.core.TaskBuilder;
import com.globant.agilepodmaster.core.Release;
import com.globant.agilepodmaster.core.Sprint;

/**
 * Builder for a backlog (backlog or sprint).
 * @author Andres Postiglioni
 *
 */
@SuppressWarnings("rawtypes")
public class BacklogBuilder extends AbstractBuilder<ReleaseBuilder, TaskBuilder> {
  private Sprint sprint;
  private Release release;

  /**
   * Constructor.
   * @param release the release.
   * @param sprintName the sprint name.
   * @param sprintNumber the sprint number.
   * @param startDate the start date.
   * @param endDate the end date.
   * @param releaseBuilder a relase builder (parent)
   */
  public BacklogBuilder(Release release, String sprintName, int sprintNumber,
      Date startDate, Date endDate, ReleaseBuilder releaseBuilder) {
    super(releaseBuilder);
    this.release = release;
    this.sprint = new Sprint(sprintName, sprintNumber, release, startDate,
        endDate);
  }

  /**
   * Constructor.
   * @param release the release.
   * @param releaseBuilder a release builder.
   */
  public BacklogBuilder(Release release, ReleaseBuilder releaseBuilder) {
    super(releaseBuilder);
    this.release = release;
  }

  /**
   * Start adding a task.
   * @return a task builder.
   */
  public TaskBuilder withTask() {
    TaskBuilder nestedBuilder = TaskBuilder.taskBuilder(release, sprint, this);
    this.addNestedBuilder(nestedBuilder);
    return nestedBuilder;
  }

  /**
   * Add this builder to the release. 
   * @return the release builder.
   */
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
