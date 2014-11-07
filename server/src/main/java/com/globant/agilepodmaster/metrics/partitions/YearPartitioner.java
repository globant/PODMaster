package com.globant.agilepodmaster.metrics.partitions;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.metrics.SprintPodMetric;

@Component
public class YearPartitioner extends Partitioner<SprintPodMetric, Partition<Integer>> {
  private static final String DIMENSION = "year";

  public YearPartitioner() {
    super(DIMENSION);
  }
  
  @Override
  public Partition<Integer> extractPartition(SprintPodMetric data) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(data.getSprint().getEndDate());
    return new Partition<Integer>(DIMENSION, calendar.get(Calendar.YEAR));
  }
}
