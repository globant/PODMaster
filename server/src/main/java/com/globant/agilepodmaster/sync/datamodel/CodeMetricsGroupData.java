package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CodeMetricsGroupData implements Serializable {

  public static enum MetricDataTrend {
    BETTER(1), SAME(0), WORSE(-1);

    private final int levelCode;

    MetricDataTrend(int levelCode) {
      this.levelCode = levelCode;
    }
  }

  public static enum MetricDataVariance {
    BIGUP(2), UP(1), NONE(0), DOWN(-1), BIGDOWN(-2);

    private final int levelCode;

    MetricDataVariance(int levelCode) {
      this.levelCode = levelCode;
    }
  }

  public String name;

  public List<CodeMetricData> metrics;

  public CodeMetricsGroupData() {
    metrics = new ArrayList<CodeMetricData>();
  }

  @Data
  public class CodeMetricData implements Serializable {

    public String key;

    public String name;

    public float value;

    public String valueFormatted;

    public MetricDataTrend trend;

    public MetricDataVariance variance;

  }
}
