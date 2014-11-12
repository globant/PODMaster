package com.globant.agilepodmaster.sync;

import com.globant.agilepodmaster.core.Organization;
import com.globant.agilepodmaster.core.Product;

public class ProductBuilder extends AbstractBuilder<OrganizationBuilder, ProjectBuilder> {
  private final Product product;

  public ProductBuilder(String productName, Organization organization, OrganizationBuilder bldr) {
    super(bldr);
    this.product = new Product(productName, organization);
  }

  public ProjectBuilder addProject(String name) {
    ProjectBuilder builder = new ProjectBuilder(name, product, this);
    this.addNestedBuilder(builder);
    return builder;
  }

//  public boolean addProject(Project project) {
//    return this.projects.add(project);
//  }

  public OrganizationBuilder addToOrganization() {
    return this.getParentBuilder();
  }

  @Override
  protected void doCollect(SnapshotBuilder snapshotBuilder) {
    snapshotBuilder.addProduct(this.product);
  }
}
