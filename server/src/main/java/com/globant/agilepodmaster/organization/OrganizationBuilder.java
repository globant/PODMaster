package com.globant.agilepodmaster.organization;

import com.globant.agilepodmaster.product.ProductBuilder;
import com.globant.agilepodmaster.snapshot.AbstractBuilder;
import com.globant.agilepodmaster.snapshot.SnapshotBuilder;

public class OrganizationBuilder extends AbstractBuilder<SnapshotBuilder, ProductBuilder> {
  private final Organization organization;
//  private final List<Product> products = new ArrayList<>();
  
  public OrganizationBuilder(String organizationName, SnapshotBuilder parentBuilder) {
    super(parentBuilder);
    this.organization = new Organization(organizationName);
  }

  public ProductBuilder addProduct(String productName) {
    ProductBuilder builder = new ProductBuilder(productName, organization, this);
    this.addNestedBuilder(builder);
    
    return builder;
  }

//  public void addProduct(Product product) {
//    products.add(product);
//  }

  public SnapshotBuilder addToSnapshot() {
    return this.getParentBuilder();
  }

  @Override
  protected void doCollect(SnapshotBuilder snapshotBuilder) {
    snapshotBuilder.addOrganization(organization);
  }
}
