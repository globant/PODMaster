package com.globant.agilepodmaster.metrics.partition;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.ProjectMetric;
import com.globant.agilepodmaster.core.Quarter;
import com.globant.agilepodmaster.core.SprintPodMetric;

@Component
public class QuarterPartitioner extends Partitioner<SprintPodMetric, Partition<Quarter>> {
  private static final String PARTITION = "quarter";

  public QuarterPartitioner() {
    super(PARTITION);
  }
  
  @Override
  public Partition<Quarter> extractPartition(SprintPodMetric data) {
    return new Partition<Quarter>(PARTITION, Quarter.toQuarter(data.getSprint().getEndDate()));
  }

  @Override
  public Partition<Quarter> extractPartition(ProjectMetric projectMetric) {
    return null;
  }
}
