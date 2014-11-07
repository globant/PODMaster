package com.globant.agilepodmaster.metrics;

import java.util.Set;

import com.globant.agilepodmaster.metrics.partitions.Partition;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MetricAggregation {
  private final Set<Partition<?>> partitions;
  private final Set<Metric<?>> metrics;

  public MetricAggregation(Set<Partition<?>> partitions, Set<Metric<?>> metrics) {
    this.partitions = partitions;
    this.metrics = metrics;
  }
}
