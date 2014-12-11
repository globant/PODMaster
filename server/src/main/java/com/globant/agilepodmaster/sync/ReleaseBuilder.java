package com.globant.agilepodmaster.sync;

import java.util.Date;

import com.globant.agilepodmaster.core.Project;
import com.globant.agilepodmaster.core.Release;

/**
 * Builds a release.
 * @author Andres Postiglioni.
 *
 */
public class ReleaseBuilder extends AbstractBuilder<ProjectBuilder, BacklogBuilder> {
  private final Release release;
  private int sprintNumber;

  /**
   * Constructor.
   * @param releaseName the release name.
   * @param project the project.
   * @param projectBuilder the project builder (parent)
   */
  public ReleaseBuilder(String releaseName, Project project, ProjectBuilder projectBuilder) {
    super(projectBuilder);
    this.release = new Release(releaseName, project);
    this.sprintNumber = 0;    
  }

  /**
   * Start adding a sprint.
   * @param name the sprint name. 
   * @param startDate the start date.
   * @param endDate the end date.
   * @return a backlog builder.
   */
  public BacklogBuilder withSprint(String name, Date startDate, Date endDate) {
    BacklogBuilder nestedBuilder = new BacklogBuilder(release, name,
        ++sprintNumber, startDate, endDate, this);
    this.addNestedBuilder(nestedBuilder);
    return nestedBuilder;
  }

  /**
   * Start adding a backlog.
   * @return a backlog builder.
   */
  public BacklogBuilder withBacklog() {
    BacklogBuilder nestedBuilder = new BacklogBuilder(release, this);
    this.addNestedBuilder(nestedBuilder);
    return nestedBuilder;
  }

  /**
   * Add this release builder to the project.
   * @return the project builder.
   */
  public ProjectBuilder addToProject() {
    return this.getParentBuilder();
  }

  @Override
  protected void doCollect(SnapshotBuilder snapshotBuilder) {
    snapshotBuilder.addRelease(release);
  }
}
