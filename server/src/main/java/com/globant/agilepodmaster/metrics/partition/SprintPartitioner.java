package com.globant.agilepodmaster.metrics.partition;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.SprintPodMetric;

@Component
public class SprintPartitioner extends Partitioner<SprintPodMetric, Partition<String>> {
  private static final String PARTITION = "sprint";

  public SprintPartitioner() {
    super(PARTITION);
  }
  
  @Override
  public Partition<String> extractPartition(SprintPodMetric data) {
    return new Partition<String>(PARTITION, data.getSprint().getName());
  }
}
