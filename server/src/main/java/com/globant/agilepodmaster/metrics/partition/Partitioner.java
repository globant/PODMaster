package com.globant.agilepodmaster.metrics.partition;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class Partitioner<T, P> {
  private final String acceptedDimension;
  
  public Partitioner(String acceptedDimension) {
    if (acceptedDimension == null) {
      throw new IllegalArgumentException("dimension must not be null");
    }

    this.acceptedDimension = acceptedDimension;
  }

  public boolean accepts(String dimension) {
    return acceptedDimension.equalsIgnoreCase(dimension);
  }
  
  @Override
  public String toString() {
    return this.acceptedDimension;
  }

  public abstract P extractPartition(T data);
}
