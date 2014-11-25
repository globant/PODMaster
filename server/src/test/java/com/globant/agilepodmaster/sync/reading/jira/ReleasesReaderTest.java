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
    
    when(mockJiraRestClient.getSprint(1,"3")).thenReturn(sprintDetails1);

    SprintReport.Sprint sprintDetails2 = new SprintReport.Sprint();
    sprintDetails2.setId(2);
    sprintDetails2.setName("Sprint 2");
    sprintDetails2.setStartDate("26/02/2001");
    sprintDetails2.setEndDate("12/03/2001");
    
    when(mockJiraRestClient.getSprint(2,"3")).thenReturn(sprintDetails2);
    
    Issue issue1 = new Issue();
    issue1.setId("id1");
    issue1.setFields(new Issue.Fields());
    issue1.getFields().setSummary("Task1");
 
    Issue issue2 = new Issue();
    issue2.setId("id2");
    issue2.setFields(new Issue.Fields());
    issue2.getFields().setSummary("Task2");    
    issue2.getFields().setParent(issue1);  
    
    Issue issue3 = new Issue();
    issue3.setId("id3");
    issue3.setFields(new Issue.Fields());
    issue3.getFields().setParent(issue1);  
    issue3.getFields().setSummary("Task3");    
    
    when(mockJiraRestClient.getSprintIssues(1)).thenReturn(
        Arrays.asList(issue1, issue2, issue3));
    
    Issue issue4 = new Issue();
    issue4.setId("id4");
    issue4.setFields(new Issue.Fields());
    issue4.getFields().setSummary("Task4");    
 
    Issue issue5 = new Issue();
    issue5.setId("id5");
    issue5.setFields(new Issue.Fields());
    issue5.getFields().setSummary("Task5");    
    issue5.getFields().setParent(issue4);   
    
    when(mockJiraRestClient.getSprintIssues(2)).thenReturn(
        Arrays.asList(issue4, issue5));
    
    Issue issue6 = new Issue();
    issue6.setId("id6");
    issue6.setFields(new Issue.Fields());
    issue6.getFields().setSummary("Task6");    
    
    Issue issue7 = new Issue();
    issue7.setId("id7");
    issue7.setFields(new Issue.Fields());
    issue7.getFields().setSummary("Task7");    
    
    when(mockJiraRestClient.getBacklogIssues("Teammate Intelligence")).thenReturn(
        Arrays.asList(issue6, issue7));

    SyncContext context = new SyncContext();
    SnapshotBuilder snapshotBuilder = new SnapshotBuilder(context);

    ReleaseReaderConfiguration.Project projectConfig = new ReleaseReaderConfiguration.Project(
        "TeammateIntelligence", "Teammate Intelligence", "3", mockJiraRestClient);
    ReleaseReaderConfiguration.Product productConfig = new ReleaseReaderConfiguration.Product(
        "FIFA14", Arrays.asList(projectConfig));
    ReleaseReaderConfiguration configuration = new ReleaseReaderConfiguration(
        "EA", Arrays.asList(productConfig));

    releasesReader.setConfiguration(configuration);
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
//    assertThat( task1.getRelease(), equalTo (null)); 
    assertThat( task1.getSprint(), equalTo(sprint1));     
    
    Task task2 = snapshot.getTasks().stream()
        .filter(s -> "Task2".equals(s.getName())).findAny().get();  
//    assertThat( task2.getRelease(), equalTo (null)); 
    assertThat( task2.getSprint(), equalTo(sprint1));   
    assertThat( task2.getParentTask(), equalTo(task1)); 
    
    Task task3 = snapshot.getTasks().stream()
        .filter(s -> "Task3".equals(s.getName())).findAny().get(); 
//    assertThat( task3.getRelease(), equalTo (null)); 
    assertThat( task3.getSprint(), equalTo(sprint1));   
    assertThat( task3.getParentTask(), equalTo(task1)); 
    
    Task task4 = snapshot.getTasks().stream()
        .filter(s -> "Task4".equals(s.getName())).findAny().get();  
//    assertThat( task4.getRelease(), equalTo (null)); 
    assertThat( task4.getSprint(), equalTo(sprint2)); 
    assertThat( task4.getParentTask(), equalTo(null)); 
    
    Task task5 = snapshot.getTasks().stream()
        .filter(s -> "Task5".equals(s.getName())).findAny().get();
//    assertThat( task5.getRelease(), equalTo (null)); 
    assertThat( task5.getSprint(), equalTo(sprint2));   
    assertThat( task5.getParentTask(), equalTo(task4)); 
    
    Task task6 = snapshot.getTasks().stream()
        .filter(s -> "Task6".equals(s.getName())).findAny().get();  
    assertThat( task6.getRelease(), equalTo(release));  
    assertThat( task6.getSprint(), equalTo(null));   
    assertThat( task6.getParentTask(), equalTo(null)); 
    
    Task task7 = snapshot.getTasks().stream()
        .filter(s -> "Task7".equals(s.getName())).findAny().get();
    assertThat( task7.getRelease(), equalTo(release));  
    assertThat( task7.getSprint(), equalTo(null));   
    assertThat( task7.getParentTask(), equalTo(null)); 
        
    verify(mockJiraRestClient).getSprintList("3");
    verify(mockJiraRestClient).getSprint(1,"3");
    verify(mockJiraRestClient).getSprint(2,"3");
    
    verify(mockJiraRestClient).getSprintIssues(1);
    verify(mockJiraRestClient).getSprintIssues(2);    
    verify(mockJiraRestClient).getBacklogIssues("Teammate Intelligence");
    
  }

}
