package com.globant.agilepodmaster.metrics.partition;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.ProjectMetric;
import com.globant.agilepodmaster.core.SprintPodMetric;

@Component
public class YearPartitioner extends Partitioner<SprintPodMetric, Partition<Integer>> {
  private static final String PARTITION = "year";

  public YearPartitioner() {
    super(PARTITION);
  }

  @Override
  public Partition<Integer> extractPartition(SprintPodMetric data) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(data.getSprint().getEndDate());
    return new Partition<Integer>(PARTITION, calendar.get(Calendar.YEAR));
  }

  @Override
  public Partition<Integer> extractPartition(ProjectMetric projectMetric) {
    return null;
  }
}
