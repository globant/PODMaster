package com.globant.agilepodmaster.sync.jira;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.globant.agilepodmaster.AbstractUnitTest;
import com.globant.agilepodmaster.sync.ConexionResponseErrorException;
import com.globant.agilepodmaster.sync.jira.JiraAPIFactory;
import com.globant.agilepodmaster.sync.jira.JiraRestClient;
import com.globant.agilepodmaster.sync.jira.responses.CustomFieldDefinition;
import com.globant.agilepodmaster.sync.jira.responses.Issue;
import com.globant.agilepodmaster.sync.jira.responses.SprintList.SprintItem;
import com.globant.agilepodmaster.sync.jira.responses.SprintReport.Sprint;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
  private MockRestServiceServer mockServer;
  private JiraRestClient jiraRestClient;

  /**
   * Creating mock and initialization.
   */
  @Before
  public void setUp() {

    RestTemplate template = new RestTemplate();
    mockServer = MockRestServiceServer.createServer(template);
    jiraRestClient = new JiraAPIFactory().withTemplate(template)
        .withCredentials("dummy", "credentials")
        .withUrlRoot("https://jira.corp.globant.com/").create();
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
    
    Sprint  sprint = jiraRestClient.getSprintReport(728, "362").getSprint();
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
            requestTo("https://jira.corp.globant.com/rest/api/latest/field"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withSuccess(
                "[{\"id\":\"customfield_10002\",\"name\":\"Story Points\","
                    + "\"custom\":true,\"orderable\":true,\"navigable\":true,\"searchable\":true,"
                    + "\"schema\":{}},"
                    + "{\"id\":\"customfield_10615\",\"name\":\"Realized Date\","
                    + "\"custom\":true,\"orderable\":true,\"navigable\":true,\"searchable\":true,"
                    + "\"schema\":{\"type\":\"date\",\"custom\":\"datepicker\",\"customId\":"
                    + "10615}},"
                    + "{\"id\":\"customfield_10106\",\"name\":\"Bug Environment\","
                    + "\"custom\":true,\"orderable\":true,\"navigable\":true,\"searchable\":true,"
                    + "\"schema\":{}},"
                    + "{\"id\":\"summary\",\"name\":\"Summary\",\"custom\":false,\"orderable\":"
                    + "true,"
                    + "\"navigable\":true,\"searchable\":true,\"schema\":{\"type\":\"string\","
                    + "\"system\":\"summary\"}}]", MediaType.APPLICATION_JSON));
    mockServer
        .expect(
            requestTo("https://jira.corp.globant.com/rest/api/latest/search?"
                + "jql=sprint%3D728&fields=key,parent,priority,summary"
                + ",components,assignee,created,issuetype,status,resolution"
                + ",resolutiondate,timetracking,labels,customfield_10106,customfield_10002"
                + "&startAt=0&maxResults=60"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withSuccess(
                "{\"issues\":[{\"id\":\"65230\",\"fields\":"
                    + " {\"summary\":\"Kickoff\"}},{\"id\":\"65227\",\"fields\":{\"summary\":"
                    + " \"Make a widget Account.\", \"customfield_10002\":2.0}}]}",
                MediaType.APPLICATION_JSON));    
    
    List<Issue> tasks = jiraRestClient.getSprintIssues(728);
    mockServer.verify();
    assertThat(tasks, hasSize(2));
    assertThat(tasks.get(1).getFields().getSummary(),
        equalTo("Make a widget Account."));
    assertThat(tasks.get(1).getFields().getStorypoints(), equalTo(2.0f));
    

  }

  /**
   * Testing method to get CustomFieldDefinitions.
   */
  @Test
  public void testCustomFieldDefinitions() {

    mockServer
        .expect(
            requestTo("https://jira.corp.globant.com/rest/api/latest/field"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withSuccess(
                "[{\"id\":\"customfield_10002\",\"name\":\"Story Points\","
                    + "\"custom\":true,\"orderable\":true,\"navigable\":true,\"searchable\":true,"
                    + "\"schema\":{}},"
                    + "{\"id\":\"customfield_10615\",\"name\":\"Realized Date\","
                    + "\"custom\":true,\"orderable\":true,\"navigable\":true,\"searchable\":true,"
                    + "\"schema\":{\"type\":\"date\",\"custom\":\"datepicker\",\"customId\":"
                    + "10615}},"
                    + "{\"id\":\"customfield_10106\",\"name\":\"Bug Environment\","
                    + "\"custom\":true,\"orderable\":true,\"navigable\":true,\"searchable\":true,"
                    + "\"schema\":{}},"
                    + "{\"id\":\"summary\",\"name\":\"Summary\",\"custom\":false,\"orderable\":"
                    + "true,"
                    + "\"navigable\":true,\"searchable\":true,\"schema\":{\"type\":\"string\","
                    + "\"system\":\"summary\"}}]", MediaType.APPLICATION_JSON));

    CustomFieldDefinition[] customFieldDefinitions = jiraRestClient
        .getCustomFieldDefinitions();

    assertThat(customFieldDefinitions, arrayWithSize(4));
    mockServer.verify();

  }
  
  @Rule
  public ExpectedException exception = ExpectedException.none();
  
  /**
   * Test what happen with a 404 response.
   */
  @Test
  public void testGetMessage_404() {
    mockServer
        .expect(
            requestTo("https://jira.corp.globant.com/rest/api/latest/field"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.NOT_FOUND));

    exception.expect(ConexionResponseErrorException.class);
    jiraRestClient.getCustomFieldDefinitions();

    mockServer.verify();

  }

}
