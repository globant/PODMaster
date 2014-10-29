package com.globant.agilepodmaster.sync.reading.jira;

import com.globant.agilepodmaster.sync.reading.jira.responses.Issue;
import com.globant.agilepodmaster.sync.reading.jira.responses.IssuesSearchResult;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList.SprintItem;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport.Sprint;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all Jira Tasks readers. TODO try to eliminate this class. It
 * is only used to reuse methods.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public abstract class BaseTaskReader extends BaseJiraReader {

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

  /**
   * Constructor.
   * 
   * @param jiraCustomSettings
   */
  public BaseTaskReader(JiraCustomSettings jiraCustomSettings) {
    super(jiraCustomSettings);
  }

  protected List<SprintItem> getSprintList() {
    ResponseEntity<SprintList> responseEntity = restClient.exchange(rootUrl
        + SPRINT_LIST_URL, HttpMethod.GET, request, SprintList.class,
        jiraCustomSettings.getJiraRapidViewId());
    SprintList sprintList = responseEntity.getBody();
    return sprintList.getSprints();
  }

  protected Sprint getSprint(final int sprintId) {
    ResponseEntity<SprintReport> responseReportEntity = restClient.exchange(
        rootUrl + SPRINT_REPORT_URL, HttpMethod.GET, request,
        SprintReport.class, jiraCustomSettings.getJiraRapidViewId(), sprintId);
    SprintReport sprintReport = responseReportEntity.getBody();
    return sprintReport.getSprint();
  }


  protected List<Issue> getSprintIssues(final int sprintId) {
    return searchIssues("sprint=" + sprintId, DEFAUL_FIELD_LIST);
  }

  protected List<Issue> getBacklogIssues() {
    return searchIssues("sprint=null AND project=\""
        + jiraCustomSettings.jiraProjectName + "\"", DEFAUL_FIELD_LIST);
  }

  private List<Issue> searchIssues(String jql, String fields) {
    List<Issue> issues = new ArrayList<Issue>();
    boolean moreContent = true;
    int startAt = 0;
    while (moreContent) {
      ResponseEntity<IssuesSearchResult> responseEntity = restClient.exchange(
          rootUrl + SEARCH_URL, HttpMethod.GET, request,
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
