package com.globant.agilepodmaster.metrics;

import java.util.Set;

import com.globant.agilepodmaster.metrics.partition.Partition;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class MetricAggregation {
  private final Set<Partition<?>> partitions;
  private final Set<Metric<?>> metrics;

  public MetricAggregation(Set<Partition<?>> partitions, Set<Metric<?>> metrics) {
    this.partitions = partitions;
    this.metrics = metrics;
  }
}
