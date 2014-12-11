package com.globant.agilepodmaster.metrics.partition;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.snapshot.ProjectMetric;
import com.globant.agilepodmaster.snapshot.ProjectPodMetric;
import com.globant.agilepodmaster.snapshot.SprintPodMetric;

/**
 * Partitioner for sprints.
 * @author Andres Postiglioni.
 *
 */
@Component
public class SprintPartitioner extends Partitioner<Partition<Integer>> {
  private static final String PARTITION = "sprint";

  /**
   * Constructor.
   */
  public SprintPartitioner() {
    super(PARTITION);
  }
  
  @Override
  public Partition<Integer> extractPartition(SprintPodMetric data) {
    return new Partition<Integer>(PARTITION, data.getSprint().getNumber());
  }

  @Override
  public Partition<Integer> extractPartition(ProjectPodMetric projectMetric) {
    return null;
  }
  
  @Override
  public Partition<Integer> extractPartition(ProjectMetric projectMetric) {
    return null;
  }
}
