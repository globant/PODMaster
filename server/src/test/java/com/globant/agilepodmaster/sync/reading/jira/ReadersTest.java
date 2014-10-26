package com.globant.agilepodmaster.sync.reading.jira;

import static org.junit.Assert.assertTrue;

import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.datamodel.ProjectDataSet;
import com.globant.agilepodmaster.sync.datamodel.SprintData;
import com.globant.agilepodmaster.sync.datamodel.TaskData;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/*
 * These are test URLs run with the browser :
 * 
 * READ FIELDS (readers initializations)
 * https://jira.corp.globant.com/rest/api/latest/field
 * 
 * READ SPRINTS LIST (to read sprint list) https://jira.corp.globant.com/rest
 * /greenhopper/1.0/sprintquery/362?includeFutureSprints
 * =true&includeHistoricSprints=true"
 * 
 * READ SPRINT REPORT (to read sprint data) https://jira.corp.globant.com/rest
 * /greenhopper/1.0/rapid/charts/sprintreport?rapidViewId=362&sprintId=728
 * 
 * // SEARCH ISSUES (to search for tasks)
 * https://jira.corp.globant.com/rest/api/latest/search?jql=sprint=null AND
 * project='Pod Master
 * '&fields=key,parent,priority,summary,components,assignee,created,issuetype,status,resolution,resolutiondate,timetracking,labels,storypoints,severity,bugpriority,bugenvironment&startAt=0&maxResults=60",
 */

public class ReadersTest {

  private SyncContext context;
  private JiraCustomSettings settings;

  public ReadersTest() {
    context = new SyncContext(1, false);
    settings = new JiraCustomSettings();
    settings.setJiraRoot("https://jira.corp.globant.com/");
    settings.setJiraProjectName("Pod Master");
    settings.setJiraRapidViewId("362");
    settings.setIgnoreTasksForUnknownMembers(false);
    settings.setTypeMapping("Story, Technical task | Sub-task, Bug");
    settings.setStatusMapping("Story, Technical task | Sub-task, Bug");
    settings.setPriorityMapping("Blocker | Critical, Major, Minor, Trivial");
    settings
        .setBugpriorityMapping("1 - Blocker | 2 - Very High, 3 - High, 4 - Medium, 5 - Low");
    settings.setSeverityMapping("Blocker, Major, Minor, Trivial");

    // set your user + password here.
    AccessToken accessToken = new AccessToken("jose.dominguez", "passwordXXXX",
        "jira access token");
    settings.setAccessToken(accessToken.Encrypt());

  }

  @Ignore
  @Test
  public void testSprintReader() {
    ProjectDataSet projectDataSet = new ProjectDataSet();

    SprintReader sprintReader = new SprintReader(context, settings);
    sprintReader.read(projectDataSet);

    List<SprintData> sprints = projectDataSet.getReleases().get(0).getSprints();
    assertTrue(sprints.size() > 0);
    assertTrue(sprints.get(0).getSprintTasks().size() > 0);

  }

  @Ignore
  @Test
  public void testBacklogReader() {

    ProjectDataSet projectDataSet = new ProjectDataSet();

    BacklogReader backlogReader = new BacklogReader(context, settings);
    backlogReader.read(projectDataSet);

    List<TaskData> backlog = projectDataSet.getReleases().get(0).getBacklog();
    assertTrue(backlog.size() > 0);

  }

}
