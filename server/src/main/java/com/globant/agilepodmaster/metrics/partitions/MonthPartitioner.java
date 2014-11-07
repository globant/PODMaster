package com.globant.agilepodmaster.metrics.partitions;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.metrics.SprintPodMetric;

@Component
public class MonthPartitioner extends Partitioner<SprintPodMetric, Partition<Integer>> {
  private static final String DIMENSION = "month";

  public MonthPartitioner() {
    super(DIMENSION);
  }

  @Override
  public Partition<Integer> extractPartition(SprintPodMetric data) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(data.getSprint().getEndDate());
    
    //Add January is represented by 0 in java.util.Calendar, so let's add one
    return new Partition<Integer>(DIMENSION, calendar.get(Calendar.MONTH) + 1);
  }
}
