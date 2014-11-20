package com.globant.agilepodmaster.metrics.partition;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.ProjectPodMetric;
import com.globant.agilepodmaster.core.Quarter;
import com.globant.agilepodmaster.core.SprintPodMetric;

@Component
public class YearQuarterPartitioner extends Partitioner<Partition<String>> {
  private static final String PARTITION = "year/quarter";

  public YearQuarterPartitioner() {
    super(PARTITION);
  }

  @Override
  public Partition<String> extractPartition(SprintPodMetric data) {
    Date date = data.getSprint().getEndDate();
    return new Partition<String>(PARTITION, Quarter.toYearQuarter(date).toString());
  }

  @Override
  public Partition<String> extractPartition(ProjectPodMetric projectMetric) {
    return null;
  }
}
