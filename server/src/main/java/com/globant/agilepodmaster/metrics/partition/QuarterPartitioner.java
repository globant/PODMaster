package com.globant.agilepodmaster.metrics.partition;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.ProjectPodMetric;
import com.globant.agilepodmaster.core.Quarter;
import com.globant.agilepodmaster.core.SprintPodMetric;

@Component
public class QuarterPartitioner extends Partitioner<Partition<Quarter>> {
  private static final String PARTITION = "quarter";

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
}
