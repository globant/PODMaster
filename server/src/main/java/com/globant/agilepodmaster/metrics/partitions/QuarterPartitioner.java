package com.globant.agilepodmaster.metrics.partitions;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.Quarter;
import com.globant.agilepodmaster.metrics.SprintPodMetric;

@Component
public class QuarterPartitioner extends Partitioner<SprintPodMetric, Partition<Quarter>>{
  private static final String DIMENSION = "quarter";

  public QuarterPartitioner() {
    super(DIMENSION);
  }
  
  @Override
  public Partition<Quarter> extractPartition(SprintPodMetric data) {
    return new Partition<Quarter>(DIMENSION, Quarter.toQuarter(data.getSprint().getEndDate()));
  }
}
