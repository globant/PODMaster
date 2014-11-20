package com.globant.agilepodmaster.metrics.partition;

import com.globant.agilepodmaster.core.ProjectPodMetric;
import com.globant.agilepodmaster.core.SprintPodMetric;

public class ProjectPartitioner extends Partitioner<Partition<String>> {
  private static final String PARTITION = "project";

  public ProjectPartitioner() {
    super(PARTITION);
  }
  
  @Override
  public Partition<String> extractPartition(SprintPodMetric sprintPodMetric) {
    String name = sprintPodMetric.getProject().getName();
    return new Partition<String>(PARTITION, name);
  }

  @Override
  public Partition<String> extractPartition(ProjectPodMetric projectMetric) {
    String name = projectMetric.getProject().getName();
    return new Partition<String>(PARTITION, name);
  }
}
