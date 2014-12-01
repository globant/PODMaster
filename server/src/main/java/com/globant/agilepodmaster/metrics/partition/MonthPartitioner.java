package com.globant.agilepodmaster.metrics.partition;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.ProjectPodMetric;
import com.globant.agilepodmaster.core.SprintPodMetric;

/**
 * Partitioner for months.
 * @author Andres Postiglioni.
 *
 */
@Component
public class MonthPartitioner extends Partitioner<Partition<Integer>> {
  private static final String PARTITION = "month";

  /**
   * Constructor.
   */
  public MonthPartitioner() {
    super(PARTITION);
  }

  @Override
  public Partition<Integer> extractPartition(SprintPodMetric data) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(data.getSprint().getEndDate());
    
    //Add January is represented by 0 in java.util.Calendar, so let's add one
    return new Partition<Integer>(PARTITION, calendar.get(Calendar.MONTH) + 1);
  }

  @Override
  public Partition<Integer> extractPartition(ProjectPodMetric projectMetric) {
    return null;
  }
}
