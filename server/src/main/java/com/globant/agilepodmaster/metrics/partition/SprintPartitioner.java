package com.globant.agilepodmaster.metrics.partition;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.ProjectPodMetric;
import com.globant.agilepodmaster.core.SprintPodMetric;

/**
 * Partitioner for sprints.
 * @author Andres Postiglioni.
 *
 */
@Component
public class SprintPartitioner extends Partitioner<Partition<String>> {
  private static final String PARTITION = "sprint";

  /**
   * Constructor.
   */
  public SprintPartitioner() {
    super(PARTITION);
  }
  
  @Override
  public Partition<String> extractPartition(SprintPodMetric data) {
    return new Partition<String>(PARTITION, data.getSprint().getName());
  }

  @Override
  public Partition<String> extractPartition(ProjectPodMetric projectMetric) {
    return null;
  }
}
