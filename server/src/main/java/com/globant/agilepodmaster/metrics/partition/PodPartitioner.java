package com.globant.agilepodmaster.metrics.partition;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.ProjectMetric;
import com.globant.agilepodmaster.core.SprintPodMetric;

@Component
public class PodPartitioner extends Partitioner<SprintPodMetric, Partition<String>> {
  private static final String PARTITION = "pod";

  public PodPartitioner() {
    super(PARTITION);
  }
  
  @Override
  public Partition<String> extractPartition(SprintPodMetric data) {
    return new Partition<String>(PARTITION, data.getPod().getName());
  }

  @Override
  public Partition<String> extractPartition(ProjectMetric projectMetric) {
    return null;
  }
}
