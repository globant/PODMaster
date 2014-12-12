package com.globant.agilepodmaster.sync.jira;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import com.globant.agilepodmaster.AbstractUnitTest;
import com.globant.agilepodmaster.release.ReleaseBuilder;
import com.globant.agilepodmaster.snapshot.Snapshot;
import com.globant.agilepodmaster.snapshot.SnapshotBuilder;
import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.jira.SprintReportProcessor;
import com.globant.agilepodmaster.sync.jira.responses.SprintReport;
import com.globant.agilepodmaster.sync.jira.responses.SprintReport.EstimateStatistic;
import com.globant.agilepodmaster.sync.jira.responses.SprintReport.Issue;
import com.globant.agilepodmaster.sync.jira.responses.SprintReport.StatFieldValue;
import com.globant.agilepodmaster.task.Task;
import com.globant.agilepodmaster.task.Task.ChangeDuringSprint;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Test how we add task associated to a sprint from a SprintReport object.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public class SprintReportProcessorTest extends AbstractUnitTest {


  @Autowired
  SprintReportProcessor sprintReportProcessor;

  private ReleaseBuilder releaseBuilder;

  private SnapshotBuilder snapshotBuilder;

  private SyncContext context = new SyncContext();

  /**
   * Setup the release Builder.
   */
  @Before
  public void setUp() {

    snapshotBuilder = new SnapshotBuilder(context);

    releaseBuilder = snapshotBuilder.withPod("POD1")
        .withPodMember("pedro", "Dominguez", "pedro").addToPod()
        .withPodMember("maria", "Gomez", "maria").addToPod().addToSnapshot()
        .withPod("POD2").withPodMember("Ruben", "Dartes", "rube").addToPod()
        .withPodMember("Juana", "Manso", "juana").addToPod().addToSnapshot()
        .withOrganization("Org Prueba").addProduct("Prod prueba")
        .addProject("Proj prueba", 500).withRelease("release1");

  }

  /**
   * Testing task properties.
   */
  @Test
  public void testShouldTaskHasThePoperties() {

    SprintReport.Sprint sprintDetails = new SprintReport.Sprint();
    sprintDetails.setId(1);
    sprintDetails.setName("Sprint 1");
    sprintDetails.setStartDate("11/02/2001");
    sprintDetails.setEndDate("25/02/2001");
    sprintDetails.setCompleteDate("26/02/2001");

    SprintReport.Contents sprintContents = new SprintReport.Contents();

    StatFieldValue statFieldValue = new StatFieldValue();
    statFieldValue.setValue(90);

    EstimateStatistic trackingStatistic = new EstimateStatistic();
    trackingStatistic.setStatFieldId("effort");
    trackingStatistic.setStatFieldValue(statFieldValue);

    Issue issue1 = new Issue();
    issue1.setId(1);
    issue1.setKey("K1");
    issue1.setEstimateStatistic(trackingStatistic);
    issue1.setAssignee("pedro");
    issue1.setSummary("K1-Summary");
    issue1.setPriorityName("critical");
    issue1.setTypeName("bug");
    issue1.setStatusName("open");

    sprintContents.setCompletedIssues(Arrays.asList(issue1));

    SprintReport report = new SprintReport();
    report.setSprint(sprintDetails);
    report.setContents(sprintContents);

    releaseBuilder = sprintReportProcessor.processClosedSprint(releaseBuilder,
        context, report);

    Snapshot snapshot = snapshotBuilder.build();

    assertThat(snapshot.getTasks(), hasSize(1));

    Task task = snapshot.getTasks().iterator().next();

    assertThat(task.getName(), equalTo("K1-Summary"));
    assertThat(task.getEffort(), equalTo(90.0));
    assertThat(task.getPriority(), equalTo(Task.Priority.CRITICAL));

    assertThat(task.getStatus(), equalTo(Task.Status.OPEN));
    assertThat(task.getType(), equalTo(Task.Type.BUG));

    assertThat(task.getOwner().getFirstName(), equalTo("pedro"));


  }


  /**
   * Testing that should add 4 different tasks in closed Sprint.
   */
  @Test
  public void testShouldAdd4DiferentTaskInClosedSprint() {

    SprintReport.Sprint sprintDetails = new SprintReport.Sprint();
    sprintDetails.setId(1);
    sprintDetails.setName("Sprint 1");
    sprintDetails.setStartDate("11/02/2001");
    sprintDetails.setEndDate("25/02/2001");
    sprintDetails.setCompleteDate("25/02/2001");

    SprintReport.Contents sprintContents = new SprintReport.Contents();

    Issue issue1 = new Issue();
    issue1.setId(1);
    issue1.setKey("K1");
    issue1.setSummary("K1-Summary");

    Issue issue2 = new Issue();
    issue2.setId(2);
    issue2.setKey("K2");
    issue2.setSummary("K2-Summary");

    sprintContents.setCompletedIssues(Arrays.asList(issue1, issue2));

    Issue issue3 = new Issue();
    issue3.setId(3);
    issue3.setKey("K3");
    issue3.setSummary("K3-Summary");

    sprintContents.setIncompletedIssues(Arrays.asList(issue3));

    Issue issue4 = new Issue();
    issue4.setId(4);
    issue4.setKey("K4");
    issue4.setSummary("K4-Summary");

    sprintContents.setPuntedIssues(Arrays.asList(issue4));

    Map<String, Boolean> addedKeys = new HashMap<String, Boolean>();
    addedKeys.put("K2", Boolean.TRUE);
    sprintContents.setIssueKeysAddedDuringSprint(addedKeys);

    SprintReport report = new SprintReport();
    report.setSprint(sprintDetails);
    report.setContents(sprintContents);

    releaseBuilder = sprintReportProcessor.processClosedSprint(releaseBuilder,
        context, report);

    Snapshot snapshot = snapshotBuilder.build();

    assertThat(snapshot.getSprints(), hasSize(1));

    assertThat(snapshot.getTasks(), hasSize(4));

    for (Task task : snapshot.getTasks()) {

      if (task.getName().equals("K1-Summary")) {
        assertThat(task.getChangeDuringSprint(),
            equalTo(ChangeDuringSprint.NOCHANGE));

      } else if (task.getName().equals("K2-Summary")) {
        assertThat(task.getChangeDuringSprint(),
            equalTo(ChangeDuringSprint.ADDED));
      } else if (task.getName().equals("K3-Summary")) {
        assertThat(task.getChangeDuringSprint(),
            equalTo(ChangeDuringSprint.REMOVED));
      } else if (task.getName().equals("K4-Summary")) {
        assertThat(task.getChangeDuringSprint(),
            equalTo(ChangeDuringSprint.REMOVED));
      }
    }

  }

  /**
   * Testing that should add 2 different task in active Sprints. Punted task are
   * considered then when we search for tasks that are associated to no sprint.
   * Issues Added During the sprint is not relevant for active sprints.
   */
  @Test
  public void testShouldAdd4DiferentTaskProperlyInActiveSprint() {

    SprintReport.Sprint sprintDetails = new SprintReport.Sprint();
    sprintDetails.setId(1);
    sprintDetails.setName("Sprint 1");

    SprintReport.Contents sprintContents = new SprintReport.Contents();

    Issue issue1 = new Issue();
    issue1.setId(1);
    issue1.setKey("K1");
    issue1.setStatusName("Closed");
    issue1.setSummary("K1-Summary");

    Issue issue2 = new Issue();
    issue2.setId(2);
    issue2.setKey("K2");
    issue2.setStatusName("Closed");
    issue2.setSummary("K2-Summary");

    sprintContents.setCompletedIssues(Arrays.asList(issue1, issue2));

    Issue issue3 = new Issue();
    issue3.setId(3);
    issue3.setKey("K3");
    issue3.setStatusName("Open");
    issue3.setSummary("K3-Summary");

    sprintContents.setIncompletedIssues(Arrays.asList(issue3));

    Issue issue4 = new Issue();
    issue4.setId(4);
    issue4.setKey("K4");
    issue4.setSummary("K4-Summary");

    sprintContents.setPuntedIssues(Arrays.asList(issue4));

    Map<String, Boolean> addedKeys = new HashMap<String, Boolean>();
    addedKeys.put("K2", Boolean.TRUE);
    sprintContents.setIssueKeysAddedDuringSprint(addedKeys);

    SprintReport report = new SprintReport();
    report.setSprint(sprintDetails);
    report.setContents(sprintContents);

    releaseBuilder = sprintReportProcessor.processActiveSprint(releaseBuilder,
        context, report);

    Snapshot snapshot = snapshotBuilder.build();

    assertThat(snapshot.getTasks(), hasSize(3));

    for (Task task : snapshot.getTasks()) {

      if (task.getName().equals("K1-Summary")) {
        assertThat(task.getChangeDuringSprint(),
            equalTo(ChangeDuringSprint.NOCHANGE));
        assertThat(task.getSprint(), equalTo(null));
        assertThat(task.getStatus(), equalTo(Task.Status.CLOSED));
      } else if (task.getName().equals("K2-Summary")) {
        assertThat(task.getChangeDuringSprint(),
            equalTo(ChangeDuringSprint.NOCHANGE));
        assertThat(task.getSprint(), equalTo(null));
        assertThat(task.getStatus(), equalTo(Task.Status.CLOSED));
      } else if (task.getName().equals("K3-Summary")) {
        assertThat(task.getChangeDuringSprint(),
            equalTo(ChangeDuringSprint.NOCHANGE));
        assertThat(task.getSprint(), equalTo(null));
        assertThat(task.getStatus(), equalTo(Task.Status.OPEN));
      }
    }

  }

  /**
   * Testing that should detect invalid Sprints.
   */
  @Test
  public void testShouldAvoidOpenSprintsInClosedSprint() {

    SyncContext context = new SyncContext();

    SprintReport.Sprint sprintDetails1 = new SprintReport.Sprint();
    sprintDetails1.setId(1);
    sprintDetails1.setName("Sprint 1");
    sprintDetails1.setStartDate("11/02/2001");
    sprintDetails1.setEndDate("25/02/2001");

    SprintReport report = new SprintReport();
    report.setSprint(sprintDetails1);

    releaseBuilder = sprintReportProcessor.processClosedSprint(releaseBuilder, context,
        report);

    assertThat(context.getLogEntries(), hasSize(1));
    assertThat(context.getLogEntries().get(0).getEventType(),
        containsString("Info"));
    assertThat(context.getLogEntries().get(0).getMessage(),
        containsString("Skipping"));

  }
  
  /**
   * Testing that should detect invalid Sprints.
   */
  @Test
  public void testShouldAvoidMissingDatesSprintsInClosedSprint() {

    SyncContext context = new SyncContext();

    SprintReport.Sprint sprintDetails1 = new SprintReport.Sprint();
    sprintDetails1.setId(1);
    sprintDetails1.setName("Sprint 1");
    sprintDetails1.setStartDate("11/02/2001");
    sprintDetails1.setCompleteDate("26/02/2001");

    SprintReport report = new SprintReport();
    report.setSprint(sprintDetails1);

    releaseBuilder = sprintReportProcessor.processClosedSprint(releaseBuilder, context,
        report);

    assertThat(context.getLogEntries(), hasSize(1));
    assertThat(context.getLogEntries().get(0).getEventType(),
        containsString("Info"));
    assertThat(context.getLogEntries().get(0).getMessage(),
        containsString("Skipping"));

  }
  /*
  private ReleaseReaderConfiguration createConfiguration(JiraRestClient jiraRestClient) {
    ReleaseReaderConfiguration.Project projectConfig = new ReleaseReaderConfiguration.Project(
        "TeammateIntelligence", "Teammate Intelligence", "3", jiraRestClient);
    ReleaseReaderConfiguration.Product productConfig = new ReleaseReaderConfiguration.Product(
        "FIFA14", Arrays.asList(projectConfig));
    ReleaseReaderConfiguration configuration = new ReleaseReaderConfiguration(
        "EA", Arrays.asList(productConfig));
    return configuration;
  }*/
}
