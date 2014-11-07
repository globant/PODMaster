package com.globant.agilepodmaster.metrics.partitions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Partition<P> {
  private final P partition;
  private final String dimension;

  public Partition(String dimension, P partition) {
    this.dimension = dimension;
    this.partition = partition;
  }
}
