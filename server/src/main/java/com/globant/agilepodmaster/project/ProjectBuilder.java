package com.globant.agilepodmaster.project;

import com.globant.agilepodmaster.product.Product;
import com.globant.agilepodmaster.product.ProductBuilder;
import com.globant.agilepodmaster.release.ReleaseBuilder;
import com.globant.agilepodmaster.snapshot.AbstractBuilder;
import com.globant.agilepodmaster.snapshot.SnapshotBuilder;

/**
 * Builder to build a project.
 * @author Andres Postiglioni.
 *
 */
public class ProjectBuilder extends AbstractBuilder<ProductBuilder, ReleaseBuilder> {
  private final Project project;

  /**
   * Constructor. 
   * @param name the name of the project.
   * @param plannedStoryPoints the planned story points for this project.
   * @param product the product.
   * @param productBuilder the product builder (parent)
   */
  public ProjectBuilder(String name, int plannedStoryPoints, Product product,
      ProductBuilder productBuilder) {
    super(productBuilder);
    this.project = new Project(name, plannedStoryPoints, product);
  }

  /**
   * Add a release to this project.
   * @param releaseName the name of the release.
   * @return a release builder.
   */
  public ReleaseBuilder withRelease(String releaseName) {
    ReleaseBuilder releaseBuilder = new ReleaseBuilder(releaseName, project, this);
    this.addNestedBuilder(releaseBuilder);
    return releaseBuilder;
  }

  /**
   * Add this project to a product.
   * @return the product builder.
   */
  public ProductBuilder addToProduct() {
    return this.getParentBuilder();
  }

  @Override
  protected void doCollect(SnapshotBuilder snapshotBuilder) {
    snapshotBuilder.addProject(project);
  }
}
