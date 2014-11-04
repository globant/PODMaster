package com.globant.agilepodmaster.sync.reading.jira;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.globant.agilepodmaster.sync.reading.jira.responses.Issue;
import com.globant.agilepodmaster.sync.reading.jira.responses.IssuesSearchResult;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList.SprintItem;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport.Sprint;

/**
 * This class gets data through Jira API Rest.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public class JiraRestClient {
  private RestTemplate restTemplate;

  private static final int MAX_SEARCH_SIZE = 60;

  private static final String DEFAUL_FIELD_LIST = "key,parent,priority,summary"
      + ",components,assignee,created,issuetype,status,resolution"
      + ",resolutiondate,timetracking,labels";

  private static final String SPRINT_LIST_URL = "rest/greenhopper/1.0/"
      + "sprintquery/{id}?includeFutureSprints=true&"
      + "includeHistoricSprints=true";

  private static final String SPRINT_REPORT_URL = "rest/greenhopper/1.0/"
      + "rapid/charts/sprintreport?rapidViewId={rapidViewId}&sprintId="
      + "{sprintId}";

  private static final String SEARCH_URL = "rest/api/latest/search?jql={jql}"
      + "&fields={fields},{customFields}&startAt={startAt}&maxResults={pageSize}";

  private List<String> customFields = new ArrayList<String>();

  protected HttpEntity<String> request;

  protected String rootUrl;

  /**
   * Constructor.
   * 
   * @param username Jira user.
   * @param password Jira pass.
   * @param jiraRoot root path where Jira is running.
   * @param jiraAPI the rest template used to access the jira api
   */

  protected JiraRestClient(
      final String username, 
      final String password,
      final String jiraRoot, 
      final RestTemplate jiraAPI) {
    final String plainCreds = username + ":" + password;
    final byte[] plainCredsBytes = plainCreds.getBytes();
    final byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    final String base64Creds = new String(base64CredsBytes);

    final HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(new MediaType("application",
        "json")));
    headers.add("Authorization", "Basic " + base64Creds);

    request = new HttpEntity<String>(headers);
    rootUrl = jiraRoot;
    restTemplate = jiraAPI;

  }

  protected List<SprintItem> getSprintList(final String rapidViewId) {
    ResponseEntity<SprintList> responseEntity = restTemplate.exchange(rootUrl
        + SPRINT_LIST_URL, HttpMethod.GET, request, SprintList.class,
        rapidViewId);
    SprintList sprintList = responseEntity.getBody();
    return sprintList.getSprints();
  }

  protected Sprint getSprint(final int sprintId, final String rapidViewId) {
    ResponseEntity<SprintReport> responseReportEntity = restTemplate.exchange(
        rootUrl + SPRINT_REPORT_URL, HttpMethod.GET, request,
        SprintReport.class, rapidViewId, sprintId);
    SprintReport sprintReport = responseReportEntity.getBody();
    return sprintReport.getSprint();
  }


  protected List<Issue> getSprintIssues(final int sprintId) {
    return searchIssues("sprint=" + sprintId, DEFAUL_FIELD_LIST);
  }

  protected List<Issue> getBacklogIssues(final String projectName) {
    return searchIssues("sprint=null AND project=\"" + projectName + "\"",
        DEFAUL_FIELD_LIST);
  }

  private List<Issue> searchIssues(String jql, String fields) {
    List<Issue> issues = new ArrayList<Issue>();
    boolean moreContent = true;
    int startAt = 0;
    while (moreContent) {
      ResponseEntity<IssuesSearchResult> responseEntity = restTemplate
          .exchange(rootUrl + SEARCH_URL, HttpMethod.GET, request,
              IssuesSearchResult.class, jql, fields,
              StringUtils.collectionToCommaDelimitedString(this.customFields),
              startAt, MAX_SEARCH_SIZE);

      IssuesSearchResult issuesSearchResult = responseEntity.getBody();

      if (CollectionUtils.isEmpty(issuesSearchResult.getIssues())) {
        break;
      }

      issues.addAll(issuesSearchResult.getIssues());

      startAt = issuesSearchResult.getStartAt() + MAX_SEARCH_SIZE + 1;
      if (startAt > issuesSearchResult.getTotal()) {
        moreContent = false;
      }
    }
    return issues;
  }


}
