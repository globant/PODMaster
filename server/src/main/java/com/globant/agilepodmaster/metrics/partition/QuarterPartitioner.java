package com.globant.agilepodmaster.metrics.partition;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.metrics.Quarter;
import com.globant.agilepodmaster.snapshot.ProjectMetric;
import com.globant.agilepodmaster.snapshot.ProjectPodMetric;
import com.globant.agilepodmaster.snapshot.SprintPodMetric;

/**
 * Partitioner for quarters.
 * @author Andres Postiglioni.
 *
 */
@Component
public class QuarterPartitioner extends Partitioner<Partition<Quarter>> {
  private static final String PARTITION = "quarter";

  /**
   * Constructor.
   */
  public QuarterPartitioner() {
    super(PARTITION);
  }
  
  @Override
  public Partition<Quarter> extractPartition(SprintPodMetric data) {
    return new Partition<Quarter>(PARTITION, Quarter.toQuarter(data.getSprint().getEndDate()));
  }

  @Override
  public Partition<Quarter> extractPartition(ProjectPodMetric projectMetric) {
    return null;
  }
  
  @Override
  public Partition<Quarter> extractPartition(ProjectMetric projectMetric) {
    return null;
  }
}
