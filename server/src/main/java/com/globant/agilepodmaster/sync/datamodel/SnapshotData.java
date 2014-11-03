package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SnapshotData implements Serializable {
  
  private Date startDate;

  private Date endDate;

  private String summary;

  private String mainClientContactName;

  private String mainClientContactEmail;

  private List<BuildMetricData> buildMetrics;

  private List<CodeMetricsGroupData> codeMetrics;

  public SnapshotData() {
    buildMetrics = new ArrayList<BuildMetricData>();
    codeMetrics = new ArrayList<CodeMetricsGroupData>();
  }
     
}
