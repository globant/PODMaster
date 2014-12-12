package com.globant.agilepodmaster.sync.jira;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Configuration for Release Reader.
 * @author jose.dominguez@globant.com
 *
 */
@Data
@AllArgsConstructor
public class ReleaseReaderConfiguration {
  
  String organizationName;
  List<Product> products;
  
  /**
   * Configuration for Release Reader Product.
   */
  @Data
  @AllArgsConstructor
  public static class Product {    
    String productName;    
    List<Project> projects;
  }
  
  /**
   * Configuration for Release Reader Project.
   */
  @Data
  @AllArgsConstructor
  public static class Project {    
    String projectName;
    String jiraName;
    String jiraRapidViewId;
    JiraRestClient jiraRestClient;
    int plannedStoryPoints;
  }
  
}
