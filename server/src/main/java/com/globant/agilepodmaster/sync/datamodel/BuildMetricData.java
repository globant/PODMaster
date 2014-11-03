package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;

public class BuildMetricData implements Serializable {
  
  private String name;

  private int buildScore;

  private int testsScore;

  private boolean noTestsExpected;

  private int testsFailedCount;

  private int testsTotalCount;
  
}
