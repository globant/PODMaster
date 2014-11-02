package com.globant.agilepodmaster.sync.reading.jira;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.globant.agilepodmaster.AbstractUnitTest;
import com.globant.agilepodmaster.sync.reading.jira.responses.Issue;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList.SprintItem;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport.Sprint;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Testing Jira responses.
 * @author jose.dominguez@globant.com
 *
 */
public class JiraRestClientTest extends AbstractUnitTest {
  
  @Autowired
  private JiraRestClient jiraRestClient;
  
  @Autowired
  private RestTemplate restTemplate;
  
  MockRestServiceServer mockServer;

  /**
   * Creating mock and initialization.
   */
  @Before
  public void setUp() {
    mockServer = MockRestServiceServer.createServer(restTemplate);
    jiraRestClient.initialize("jose.dominguez", "qwert",
        "https://jira.corp.globant.com/");
  }

  /**
   * Testing response with sprints.
   */
  @Test
  public void testSprintList() {
    
    mockServer
        .expect(
            requestTo("https://jira.corp.globant.com/rest/greenhopper/1.0/"
        + "sprintquery/362?includeFutureSprints=true&includeHistoricSprints=true"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withSuccess(
                "{\"sprints\":[{\"id\":728},{\"id\":755},{\"id\":793}],\"rapidViewId\":362}",
                MediaType.APPLICATION_JSON));
    
    List<SprintItem>  sprints = jiraRestClient.getSprintList("362");
    mockServer.verify();
    assertThat(sprints, hasSize(3) );
    
  }

  /**
   * Testing response after asking for sprint description.
   */
  @Test
  public void testSprint() {
    
    mockServer
        .expect(
            requestTo("https://jira.corp.globant.com/rest/greenhopper/1.0/"
                + "rapid/charts/sprintreport?rapidViewId=362&sprintId=728"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
                withSuccess("{\"contents\":{},"
                    + " \"sprint\":"
                    + "         {\"id\":728, \"name\":\"sprintA\"}}}"
                           
                ,MediaType.APPLICATION_JSON));
    
    Sprint  sprint = jiraRestClient.getSprint(728, "362");
    mockServer.verify();
    assertThat(sprint.getName(), equalTo("sprintA") );
    
  }
  
  /**
   * Testing response after asking for sprint tasks.
   */
  @Test
  public void testSprintIssues() {

    mockServer
        .expect(
            requestTo("https://jira.corp.globant.com/rest/api/latest/search?"
                + "jql=sprint%3D728&fields=key,parent,priority,summary"
                + ",components,assignee,created,issuetype,status,resolution"
                + ",resolutiondate,timetracking,labels,&startAt=0&maxResults=60"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withSuccess(
                "{\"issues\":[{\"id\":\"65230\",\"fields\":"
                    + " {\"summary\":\"Kickoff\"}},{\"id\":\"65227\",\"fields\":{\"summary\":"
                    + " \"Make a widget Account.\"}}]}",
                MediaType.APPLICATION_JSON));
    List<Issue> tasks = jiraRestClient.getSprintIssues(728);
    mockServer.verify();
    assertThat(tasks, hasSize(2));
    assertThat(tasks.get(1).getFields().getSummary(),
        equalTo("Make a widget Account."));

  }

}
