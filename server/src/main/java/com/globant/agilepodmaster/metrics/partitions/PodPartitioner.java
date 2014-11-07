package com.globant.agilepodmaster.metrics.partitions;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.metrics.SprintPodMetric;

@Component
public class PodPartitioner extends Partitioner<SprintPodMetric, Partition<String>> {
  private static final String DIMENSION = "pod";

  public PodPartitioner() {
    super(DIMENSION);
  }
  
  @Override
  public Partition<String> extractPartition(SprintPodMetric data) {
    return new Partition<String>(DIMENSION, data.getPod().getName());
  }
}
