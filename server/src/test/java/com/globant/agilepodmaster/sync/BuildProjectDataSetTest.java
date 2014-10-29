package com.globant.agilepodmaster.sync;

import static org.junit.Assert.assertTrue;

import com.globant.agilepodmaster.sync.datamodel.ProjectDataSet;
import com.globant.agilepodmaster.sync.datamodel.SprintData;
import com.globant.agilepodmaster.sync.datamodel.TaskData;
import com.globant.agilepodmaster.sync.reading.jira.AccessToken;
import com.globant.agilepodmaster.sync.reading.jira.JiraCustomSettings;
import com.globant.agilepodmaster.sync.reading.jira.ReleaseReader;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class BuildProjectDataSetTest {
  
  private SyncContext context;
  private JiraCustomSettings settings;

  public BuildProjectDataSetTest() {
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

    AccessToken accessToken = new AccessToken("jose.dominguez", "XXXX",
        "jira access token");
    settings.setAccessToken(accessToken.Encrypt());
  }

  @Ignore
  @Test
  public void test() {
    
    ReleaseReader releaseReader = new ReleaseReader(settings);   
    
    ProjectDataSet projectDataSet = new ProjectDataSet.Builder(context)
       .add(releaseReader)
       .build();
    
    List<SprintData> sprints = projectDataSet.getReleases().get(0).getSprints();
    assertTrue(sprints.size() > 0);
    assertTrue(sprints.get(0).getSprintTasks().size() > 0);
    
    List<TaskData> backlog = projectDataSet.getReleases().get(0).getBacklog();
    assertTrue(backlog.size() > 0);
    
  }

}
