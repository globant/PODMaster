package com.globant.agilepodmaster.sync;

import java.util.Date;

import com.globant.agilepodmaster.core.Project;
import com.globant.agilepodmaster.core.Release;

public class ReleaseBuilder extends AbstractBuilder<ProjectBuilder, BacklogBuilder> {
  private final Release release;

  public ReleaseBuilder(String releaseName, Project project, ProjectBuilder projectBuilder) {
    super(projectBuilder);
    this.release = new Release(releaseName, project);
  }

  public BacklogBuilder withSprint(String name, Date startDate, Date endDate) {
    BacklogBuilder nestedBuilder = new BacklogBuilder(release, name, startDate, endDate, this);
    this.addNestedBuilder(nestedBuilder);
    return nestedBuilder;
  }

  public BacklogBuilder withBacklog() {
    BacklogBuilder nestedBuilder = new BacklogBuilder(release, this);
    this.addNestedBuilder(nestedBuilder);
    return nestedBuilder;
  }

  public ProjectBuilder addToProject() {
    return this.getParentBuilder();
  }

  @Override
  protected void doCollect(SnapshotBuilder snapshotBuilder) {
    snapshotBuilder.addRelease(release);
  }
}
