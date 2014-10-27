package com.globant.agilepodmaster.product;

public class DummyProductReader implements ProductReader {
  @Override
  public void readInto(ProductBuilder builder) {
    builder.withProduct("productName")
      .addProject("Project name").withBacklog()
        .addTask(1).inSprint(1).assignedTo("someone@globant.com")
        .addTask(2).inSprint(2)
        .addTask(3);
  }
}
