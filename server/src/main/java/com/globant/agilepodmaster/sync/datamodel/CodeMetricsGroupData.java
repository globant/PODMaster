package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CodeMetricsGroupData implements Serializable {

  private static enum MetricDataTrend {
    BETTER(1), SAME(0), WORSE(-1);

    private final int levelCode;

    MetricDataTrend(int levelCode) {
      this.levelCode = levelCode;
    }
  }

  private static enum MetricDataVariance {
    BIGUP(2), UP(1), NONE(0), DOWN(-1), BIGDOWN(-2);

    private final int levelCode;

    MetricDataVariance(int levelCode) {
      this.levelCode = levelCode;
    }
  }

  private String name;

  private List<CodeMetricData> metrics;

  private CodeMetricsGroupData() {
    metrics = new ArrayList<CodeMetricData>();
  }

  @Data
  private class CodeMetricData implements Serializable {

    private String key;

    private String name;

    private float value;

    private String valueFormatted;

    private MetricDataTrend trend;

    private MetricDataVariance variance;

  }
}
