package com.globant.agilepodmaster.sync.reading.jira;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.globant.agilepodmaster.AbstractUnitTest;
import com.globant.agilepodmaster.core.Organization;
import com.globant.agilepodmaster.core.Product;
import com.globant.agilepodmaster.core.Project;
import com.globant.agilepodmaster.core.Release;
import com.globant.agilepodmaster.core.Snapshot;
import com.globant.agilepodmaster.core.Sprint;
import com.globant.agilepodmaster.core.Task;
import com.globant.agilepodmaster.sync.ReleaseReaderConfiguration;
import com.globant.agilepodmaster.sync.SnapshotBuilder;
import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.reading.jira.responses.Issue;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * Testing ReleasesReader class.
 * @author jose.dominguez@globant.com
 *
 */
public class ReleasesReaderTest extends AbstractUnitTest {

  @Autowired
  ReleasesReader releasesReader;
  
  /**
   * Testing readInto.
   */
  @Test
  @SuppressWarnings("PMD")
  public void testReadIntoHappyPath() {

    // mock creation
    JiraRestClient mockJiraRestClient = mock(JiraRestClient.class);
    
    SprintList.SprintItem sprintItem1 = new SprintList.SprintItem();
    sprintItem1.setName("Sprint 1");
    sprintItem1.setId(1);
    SprintList.SprintItem sprintItem2 = new SprintList.SprintItem();
    sprintItem2.setName("Sprint 2");
    sprintItem2.setId(2);

    when(mockJiraRestClient.getSprintList("3")).thenReturn(
        Arrays.asList(sprintItem1, sprintItem2));
    
    SprintReport.Sprint sprintDetails1 = new SprintReport.Sprint();
    sprintDetails1.setId(1);
    sprintDetails1.setName("Sprint 1");
    sprintDetails1.setStartDate("11/02/2001");
    sprintDetails1.setEndDate("25/02/2001");
    sprintDetails1.setCompleteDate("26/02/2001");
    
    SprintReport sprintReport1 = new SprintReport();
    
    SprintReport.Issue issue1 = new SprintReport.Issue();
    issue1.setKey("id1");
    issue1.setSummary("Task1");
    
    SprintReport.Issue issue2 = new SprintReport.Issue();
    issue2.setKey("id2");
    issue2.setSummary("Task2");
    
    SprintReport.Issue issue3 = new SprintReport.Issue();
    issue3.setKey("id3");
    issue3.setSummary("Task3");
    
    SprintReport.Contents sprintContents1 = new SprintReport.Contents();
    sprintContents1.setCompletedIssues(Arrays.asList(issue1, issue2 ,issue3));
    
    sprintReport1.setSprint(sprintDetails1);
    sprintReport1.setContents(sprintContents1);
    
    when(mockJiraRestClient.getSprintReport(1,"3")).thenReturn(sprintReport1);

    SprintReport.Sprint sprintDetails2 = new SprintReport.Sprint();
    sprintDetails2.setId(2);
    sprintDetails2.setName("Sprint 2");
    sprintDetails2.setStartDate("26/02/2001");
    sprintDetails2.setEndDate("12/03/2001");
    sprintDetails2.setCompleteDate("26/04/2001");
    
    SprintReport sprintReport2 = new SprintReport();
    
    SprintReport.Issue issue4 = new SprintReport.Issue();
    issue4.setKey("id4");
    issue4.setSummary("Task4");
    
    SprintReport.Issue issue5 = new SprintReport.Issue();
    issue5.setKey("id5");
    issue5.setSummary("Task5");
    
    SprintReport.Contents sprintContents2 = new SprintReport.Contents();
    sprintContents2.setCompletedIssues(Arrays.asList(issue4, issue5));
    
    sprintReport2.setSprint(sprintDetails2);
    sprintReport2.setContents(sprintContents2);
    
    when(mockJiraRestClient.getSprintReport(2,"3")).thenReturn(sprintReport2);
    
    Issue issue6 = new Issue();
    issue6.setId("id6");
    issue6.setFields(new Issue.Fields());
    issue6.getFields().setSummary("Task6");    
    
    Issue issue7 = new Issue();
    issue7.setId("id7");
    issue7.setFields(new Issue.Fields());
    issue7.getFields().setParent(issue6);  
    issue7.getFields().setSummary("Task7");    
    
    when(mockJiraRestClient.getBacklogIssues("Teammate Intelligence")).thenReturn(
        Arrays.asList(issue6, issue7));

    SyncContext context = new SyncContext();
    SnapshotBuilder snapshotBuilder = new SnapshotBuilder(context);
    
    releasesReader.setConfiguration(createConfiguration(mockJiraRestClient));
    releasesReader.readInto(snapshotBuilder, context);
    
    Snapshot snapshot = snapshotBuilder.build();
    
    assertThat( snapshot.getOrganizations(), hasSize(1));
    Organization organization = snapshot.getOrganizations().stream()
        .filter(s -> "EA".equals(s.getName())).findAny().get();
    assertThat( organization, notNullValue());
    
    assertThat( snapshot.getProducts(), hasSize(1));
    Product product = snapshot.getProducts().stream()
        .filter(s -> "FIFA14".equals(s.getName())).findAny().get();
    
    assertThat( snapshot.getProjects(), hasSize(1));
    Project project = snapshot.getProjects().stream()
        .filter(s -> "TeammateIntelligence".equals(s.getName())).findAny().get();   
    assertThat( project.getProduct(), equalTo(product));  
    
    assertThat( snapshot.getReleases(), hasSize(1));
    Release release = snapshot.getReleases().stream()
        .filter(s -> "FAKE Release".equals(s.getName())).findAny().get();
    
    assertThat( snapshot.getSprints(), hasSize(2));
    Sprint sprint1 = snapshot.getSprints().stream()
        .filter(s -> "Sprint 1".equals(s.getName())).findAny().get();    
    assertThat( sprint1.getRelease(), equalTo(release));  
    
    Sprint sprint2 = snapshot.getSprints().stream()
        .filter(s -> "Sprint 2".equals(s.getName())).findAny().get();
    
    assertThat( snapshot.getTasks(), hasSize(7));
    
    Task task1 = snapshot.getTasks().stream()
        .filter(s -> "Task1".equals(s.getName())).findAny().get();
    assertThat( task1.getSprint(), equalTo(sprint1));     
    
    Task task2 = snapshot.getTasks().stream()
        .filter(s -> "Task2".equals(s.getName())).findAny().get();   
    assertThat( task2.getSprint(), equalTo(sprint1));   
    assertThat( task2.getParentTask(), equalTo(null)); 
    
    Task task3 = snapshot.getTasks().stream()
        .filter(s -> "Task3".equals(s.getName())).findAny().get(); 
    assertThat( task3.getSprint(), equalTo(sprint1));   
    assertThat( task3.getParentTask(), equalTo(null)); 
    
    Task task4 = snapshot.getTasks().stream()
        .filter(s -> "Task4".equals(s.getName())).findAny().get();  
    assertThat( task4.getSprint(), equalTo(sprint2)); 
    assertThat( task4.getParentTask(), equalTo(null)); 
    
    Task task5 = snapshot.getTasks().stream()
        .filter(s -> "Task5".equals(s.getName())).findAny().get();
    assertThat( task5.getSprint(), equalTo(sprint2));   
    assertThat( task5.getParentTask(), equalTo(null)); 
    
    Task task6 = snapshot.getTasks().stream()
        .filter(s -> "Task6".equals(s.getName())).findAny().get();  
    assertThat( task6.getRelease(), equalTo(release));  
    assertThat( task6.getSprint(), equalTo(null));   
    assertThat( task6.getParentTask(), equalTo(null)); 
    
    Task task7 = snapshot.getTasks().stream()
        .filter(s -> "Task7".equals(s.getName())).findAny().get();
    assertThat( task7.getRelease(), equalTo(release));  
    assertThat( task7.getSprint(), equalTo(null));   
    assertThat( task7.getParentTask(), equalTo(task6)); 
        
    verify(mockJiraRestClient).getSprintList("3");
    verify(mockJiraRestClient).getSprintReport(1,"3");
    verify(mockJiraRestClient).getSprintReport(2,"3");
       
    verify(mockJiraRestClient).getBacklogIssues("Teammate Intelligence");
    
  }
  
  private ReleaseReaderConfiguration createConfiguration(JiraRestClient jiraRestClient) {
    ReleaseReaderConfiguration.Project projectConfig = new ReleaseReaderConfiguration.Project(
        "TeammateIntelligence", "Teammate Intelligence", "3", jiraRestClient);
    ReleaseReaderConfiguration.Product productConfig = new ReleaseReaderConfiguration.Product(
        "FIFA14", Arrays.asList(projectConfig));
    ReleaseReaderConfiguration configuration = new ReleaseReaderConfiguration(
        "EA", Arrays.asList(productConfig));
    return configuration;
  }

}
