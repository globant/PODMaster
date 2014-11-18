package com.globant.agilepodmaster.metrics.partition;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Partition<P> {
  private final P key;
  private final String partition;

  public Partition(String partition, P key) {
    this.partition = partition;
    this.key = key;
  }
}
