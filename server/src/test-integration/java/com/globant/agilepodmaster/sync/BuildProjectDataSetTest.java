package com.globant.agilepodmaster.sync;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.globant.agilepodmaster.AbstractIntegrationTest;
import com.globant.agilepodmaster.dashboard.DashboardRepository;
import com.globant.agilepodmaster.sync.datamodel.ProjectDataSet;
import com.globant.agilepodmaster.sync.datamodel.ProjectDataSetBuilder;
import com.globant.agilepodmaster.sync.datamodel.SprintData;
import com.globant.agilepodmaster.sync.datamodel.TaskData;
import com.globant.agilepodmaster.sync.reading.jira.JiraAPIFactory;
import com.globant.agilepodmaster.sync.reading.jira.JiraCustomSettings;
import com.globant.agilepodmaster.sync.reading.jira.JiraRestClient;
import com.globant.agilepodmaster.sync.reading.jira.ReleasesReader;

public class BuildProjectDataSetTest extends AbstractIntegrationTest {
  private SyncContext context;
  private JiraCustomSettings settings;
  
  @Autowired
  ReleasesReader releasesReader;

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  DashboardRepository repo;
 
  @Value("${podmaster.test.jira.username}")
  private String username;
  
  @Value("${podmaster.test.jira.password}")
  private String password;
  
  @Value("${podmaster.test.jira.urlroot:https://jira.corp.globant.com/}")
  private String urlRoot;

  public BuildProjectDataSetTest() {
    context = new SyncContext(1, false);

    settings = new JiraCustomSettings();
    settings.setJiraProjectName("Pod Master");
    settings.setJiraRapidViewId("362");
    settings.setIgnoreTasksForUnknownMembers(false);
    settings.setTypeMapping("Story, Technical task | Sub-task, Bug");
    settings.setStatusMapping("Story, Technical task | Sub-task, Bug");
    settings.setPriorityMapping("Blocker | Critical, Major, Minor, Trivial");
    settings
        .setBugpriorityMapping("1 - Blocker | 2 - Very High, 3 - High, 4 - Medium, 5 - Low");
    settings.setSeverityMapping("Blocker, Major, Minor, Trivial");    
  }
  
  @Ignore @Test
  public void test() {
    JiraRestClient jiraRestClient = new JiraAPIFactory()
      .withCredentials(username, password)
      .withTemplate(restTemplate)
      .withUrlRoot(urlRoot)
      .create();

    releasesReader.setRestJiraClient(jiraRestClient);
    releasesReader.setSettings(settings);

    ProjectDataSet projectDataSet = new ProjectDataSetBuilder(context)
        .addReader(releasesReader).build();

    List<SprintData> sprints = projectDataSet.getReleases().get(0).getSprints();
    assertTrue(sprints.size() > 0);
    assertTrue(sprints.get(0).getSprintTasks().size() > 0);

    List<TaskData> backlog = projectDataSet.getReleases().get(0).getBacklog();
    assertTrue(backlog.size() > 0);
  }
}
