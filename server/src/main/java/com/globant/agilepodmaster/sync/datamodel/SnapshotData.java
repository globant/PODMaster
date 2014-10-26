package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SnapshotData implements Serializable {
  
  public Date startDate;

  public Date endDate;

  public String summary;

  public String mainClientContactName;

  public String mainClientContactEmail;

  public List<BuildMetricData> buildMetrics;

  public List<CodeMetricsGroupData> codeMetrics;

  public SnapshotData() {
    buildMetrics = new ArrayList<BuildMetricData>();
    codeMetrics = new ArrayList<CodeMetricsGroupData>();
  }
     
}
