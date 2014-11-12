package com.globant.agilepodmaster.sync;

import com.globant.agilepodmaster.core.Product;
import com.globant.agilepodmaster.core.Project;

public class ProjectBuilder extends AbstractBuilder<ProductBuilder, ReleaseBuilder>{
  private final Project project;

  public ProjectBuilder(String name, Product product, ProductBuilder productBuilder) {
    super(productBuilder);
    this.project = new Project(name, product);
  }

  public ReleaseBuilder withRelease(String releaseName) {
    ReleaseBuilder releaseBuilder = new ReleaseBuilder(releaseName, project, this);
    this.addNestedBuilder(releaseBuilder);
    return releaseBuilder;
  }

  public ProductBuilder addToProduct() {
    return this.getParentBuilder();
  }

  @Override
  protected void doCollect(SnapshotBuilder snapshotBuilder) {
    snapshotBuilder.addProject(project);
  }
}
