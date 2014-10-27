package com.globant.agilepodmaster.sync.reading.jira;
import lombok.Data;
/**
 * This setting are only for Jira. That is why we store them serialized in the
 * DB.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Data
public class JiraCustomSettings {

  public String jiraRoot;

  public String jiraProjectName;

  public String jiraRapidViewId;

  public boolean ignoreTasksForUnknownMembers;

  public boolean inheritAssigneeFromParent;

  public String typeMapping;

  public String statusMapping;

  public String priorityMapping;

  public String bugpriorityMapping;

  public String severityMapping;

  public String externalBehavior;

  public String externalMapping;

  public String accessToken;

}
