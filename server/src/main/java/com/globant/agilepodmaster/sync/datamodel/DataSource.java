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
 
  private static enum DataSourceType {
    Jira, Jenkins
  }

  private String serializedSettings;

  private boolean syncSnapshot;

  private boolean syncBacklog;

  private boolean syncSprints;

  private boolean syncPods;

  private boolean syncNonWorkingDays;

  private boolean syncHighlights;

  private boolean syncRisks;

  private boolean syncBuildMetrics;

  private boolean syncCodeMetrics;

  private DataSourceType dataSourceType;

  private byte[] bytes;


}
