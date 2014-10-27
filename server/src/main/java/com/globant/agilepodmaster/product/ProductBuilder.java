package com.globant.agilepodmaster.product;

import com.globant.agilepodmaster.project.ProjectBuilder;

public interface ProductBuilder {
  ProductBuilder withProduct(String string);
  ProjectBuilder addProject(String name);
}
