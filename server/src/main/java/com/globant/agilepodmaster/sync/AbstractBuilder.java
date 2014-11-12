package com.globant.agilepodmaster.sync;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

public abstract class AbstractBuilder<P, T extends SnapshotDataCollector> implements
    SnapshotDataCollector {

  private final List<T> nestedBuilders = new LinkedList<T>();

  @Getter
  private final P parentBuilder;

  protected AbstractBuilder(P parentBuilder) {
    this.parentBuilder = parentBuilder;
  }

  public void addNestedBuilder(T builder) {
    nestedBuilders.add(builder);
  }

  public void collect(SnapshotBuilder snapshotBuilder) {
    this.doCollect(snapshotBuilder);
    nestedBuilders.forEach(p -> p.collect(snapshotBuilder));
  }

  protected abstract void doCollect(SnapshotBuilder snapshotBuilder);
}
