package com.globant.agilepodmaster.sync.datamodel;

import lombok.Data;

/**
 * Configuration of the data source. Tell which group of information manages
 * 
 * @author jose.dominguez
 *
 */
@Data
public class DataSource {
 
  public static enum DataSourceType {
    Jira, Jenkins
  }

  public String serializedSettings;

  public boolean syncSnapshot;

  public boolean syncBacklog;

  public boolean syncSprints;

  public boolean syncPods;

  public boolean syncNonWorkingDays;

  public boolean syncHighlights;

  public boolean syncRisks;

  public boolean syncBuildMetrics;

  public boolean syncCodeMetrics;

  public DataSourceType dataSourceType;

  public byte[] bytes;


}
