package com.globant.agilepodmaster.product;

import com.globant.agilepodmaster.organization.Organization;
import com.globant.agilepodmaster.organization.OrganizationBuilder;
import com.globant.agilepodmaster.project.ProjectBuilder;
import com.globant.agilepodmaster.snapshot.AbstractBuilder;
import com.globant.agilepodmaster.snapshot.SnapshotBuilder;

/**
 * Builder for a Product. 
 * @author jose.dominguez@globant.com
 *
 */
public class ProductBuilder extends AbstractBuilder<OrganizationBuilder, ProjectBuilder> {
  private final Product product;

  /**
   * Constructor.
   * @param productName the name of the product.
   * @param organization the organization.
   * @param bldr the organization builder.
   */
  public ProductBuilder(String productName, Organization organization, OrganizationBuilder bldr) {
    super(bldr);
    this.product = new Product(productName, organization);
  }

  /**
   * Add a project to this product.
   * @param name the name of the project.
   * @param plannedStoryPoints the planned story points of the project.
   * @return a project builder.
   */
  public ProjectBuilder addProject(String name, int plannedStoryPoints) {
    ProjectBuilder builder = new ProjectBuilder(name, plannedStoryPoints, product, this);
    this.addNestedBuilder(builder);
    return builder;
  }

  /**
   * Add this product to an organization.
   * @return the organization builder.
   */
  public OrganizationBuilder addToOrganization() {
    return this.getParentBuilder();
  }

  @Override
  protected void doCollect(SnapshotBuilder snapshotBuilder) {
    snapshotBuilder.addProduct(this.product);
  }
}
