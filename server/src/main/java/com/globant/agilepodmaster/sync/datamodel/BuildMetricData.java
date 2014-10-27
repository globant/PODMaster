package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;

public class BuildMetricData implements Serializable {
  
  public String name;

  public int buildScore;

  public int testsScore;

  public boolean noTestsExpected;

  public int testsFailedCount;

  public int testsTotalCount;
  
}
