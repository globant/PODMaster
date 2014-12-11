package com.globant.agilepodmaster.metrics.partition;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.ProjectMetric;
import com.globant.agilepodmaster.core.ProjectPodMetric;
import com.globant.agilepodmaster.core.SprintPodMetric;

/**
 * Partitioner for year.
 * @author Andres Postiglioni.
 *
 */
@Component
public class YearPartitioner extends Partitioner<Partition<Integer>> {
  private static final String PARTITION = "year";

  /**
   * Constructor.
   */
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
  public Partition<Integer> extractPartition(ProjectPodMetric projectMetric) {
    return null;
  }
  
  @Override
  public Partition<Integer> extractPartition(ProjectMetric projectMetric) {
    return null;
  }
}
