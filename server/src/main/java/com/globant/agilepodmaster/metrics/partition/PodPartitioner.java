package com.globant.agilepodmaster.metrics.partition;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.ProjectPodMetric;
import com.globant.agilepodmaster.core.SprintPodMetric;


/**
 * Partitioner for pods..
 * @author Andres Postiglioni.
 *
 */
@Component
public class PodPartitioner extends Partitioner<Partition<String>> {
  private static final String PARTITION = "pod";

  /**
   * Constructor.
   */
  public PodPartitioner() {
    super(PARTITION);
  }
  
  @Override
  public Partition<String> extractPartition(SprintPodMetric data) {
    return new Partition<String>(PARTITION, data.getPod().getName());
  }

  @Override
  public Partition<String> extractPartition(ProjectPodMetric data) {
    return new Partition<String>(PARTITION, data.getPod().getName());
  }
}
