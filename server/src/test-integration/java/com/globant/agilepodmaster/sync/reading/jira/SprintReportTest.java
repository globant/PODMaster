package com.globant.agilepodmaster.sync.reading.jira;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import com.globant.agilepodmaster.AbstractIntegrationTest;
import com.globant.agilepodmaster.readers.releases.JiraAPIFactory;
import com.globant.agilepodmaster.readers.releases.JiraRestClient;
import com.globant.agilepodmaster.readers.releases.jira.responses.SprintReport;
import com.globant.agilepodmaster.readers.releases.jira.responses.SprintReport.Issue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Test for testing the reading of a SprintReport from our Jira for testing.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public class SprintReportTest extends AbstractIntegrationTest {

  @Autowired
  RestTemplate restTemplate;

  private JiraRestClient jiraRestClient;

  /**
   * Set up for all tests.
   */
  @Before
  public void setUp() {
    jiraRestClient = new JiraAPIFactory()
        .withCredentials("jose.dominguez", "Jose1234")
        .withTemplate(restTemplate)
        .withUrlRoot("http://nglb008dxu00.tx.corp.globant.com:8080/").create();
  }

  /**
   * Test if 2 added task were well indicated.
   */
  @Test
  public void testShouldDetect2TaskAddedDuringSprint() {

    SprintReport sprintReport = jiraRestClient.getSprintReport(36, "11");

    Map<String, Boolean> issuesAdded = sprintReport.getContents()
        .getIssueKeysAddedDuringSprint();

    assertThat(issuesAdded.entrySet(), hasSize(2));
    assertThat(issuesAdded, hasEntry("PM-9", Boolean.TRUE));
    assertThat(issuesAdded, hasEntry("PM-12", Boolean.TRUE));

  }

  /**
   * Test if 2 removed tasks were well indicated.
   */
  @Test
  public void testShouldDetect2TaskRemovedDuringSprint() {

    SprintReport sprintReport = jiraRestClient.getSprintReport(35, "11");

    List<Issue> puntedIssues = sprintReport.getContents().getPuntedIssues();

    assertThat(puntedIssues, hasSize(1));
    assertThat(puntedIssues.get(0).getKey(), equalTo("PM-12"));

    assertThat(puntedIssues.get(0).getEstimateStatistic().getStatFieldValue()
        .getValue(), equalTo(40));

  }


}
