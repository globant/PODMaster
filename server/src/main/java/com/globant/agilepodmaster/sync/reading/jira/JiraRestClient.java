package com.globant.agilepodmaster.sync.reading.jira;

import com.globant.agilepodmaster.sync.DataSourceConexionException;
import com.globant.agilepodmaster.sync.EncryptionException;
import com.globant.agilepodmaster.sync.reading.jira.responses.CustomFieldDefinition;
import com.globant.agilepodmaster.sync.reading.jira.responses.Issue;
import com.globant.agilepodmaster.sync.reading.jira.responses.IssuesSearchResult;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList.SprintItem;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport.Sprint;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * This class gets data through Jira API Rest.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Slf4j
public class JiraRestClient {

  private RestTemplate restTemplate;

  private static final int MAX_SEARCH_SIZE = 60;

  private static final String UTF8_CHARSET = "UTF8";

  private static final String DEFAUL_FIELD_LIST = "key,parent,priority,summary"
      + ",components,assignee,created,issuetype,status,resolution"
      + ",resolutiondate,timetracking,labels";

  private static final String FIELD_LIST_URL = "rest/api/latest/field";

  private static final String SPRINT_LIST_URL = "rest/greenhopper/1.0/"
      + "sprintquery/{id}?includeFutureSprints=true&"
      + "includeHistoricSprints=true";

  private static final String SPRINT_REPORT_URL = "rest/greenhopper/1.0/"
      + "rapid/charts/sprintreport?rapidViewId={rapidViewId}&sprintId="
      + "{sprintId}";

  private static final String SEARCH_URL = "rest/api/latest/search?jql={jql}"
      + "&fields={fields},{customFields}&startAt={startAt}&maxResults={pageSize}";


  protected HttpEntity<String> request;

  protected String rootUrl;

  protected CustomFieldDefinition[] customFieldDefinitions;

  protected Map<String, String> replacements;

  /**
   * Constructor.
   * 
   * @param username Jira user.
   * @param password Jira pass.
   * @param jiraRoot root path where Jira is running.
   * @param jiraAPI the rest template used to access the jira api
   */
  protected JiraRestClient(final String username, final String password,
      final String jiraRoot, final RestTemplate jiraAPI) {
    final String plainCreds = username + ":" + password;

    String base64Creds;
    try {
      final byte[] plainCredsBytes = plainCreds.getBytes(UTF8_CHARSET);
      final byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
      base64Creds = new String(base64CredsBytes, UTF8_CHARSET);
    } catch (UnsupportedEncodingException e) {
      throw new EncryptionException(e.getMessage(), e);
    }

    final HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(new MediaType("application",
        "json")));
    headers.add("Authorization", "Basic " + base64Creds);

    request = new HttpEntity<String>(headers);
    rootUrl = jiraRoot;

    restTemplate = jiraAPI;

  }

  protected CustomFieldDefinition[] getCustomFieldDefinitions() {
    ResponseEntity<CustomFieldDefinition[]> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(rootUrl + FIELD_LIST_URL,
          HttpMethod.GET, request, CustomFieldDefinition[].class);
    } catch (RestClientException e) {
      log.error("Cannot get custom field definitions", e);
      throw new DataSourceConexionException(
          "Cannot get custom field definitions", e);
    }
    return responseEntity.getBody();
  }

  protected List<SprintItem> getSprintList(final String rapidViewId) {
    ResponseEntity<SprintList> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(rootUrl + SPRINT_LIST_URL,
          HttpMethod.GET, request, SprintList.class, rapidViewId);
    } catch (RestClientException e) {
      log.error("Cannot get sprint list", e);
      throw new DataSourceConexionException("Cannot get sprint list", e);
    }
    SprintList sprintList = responseEntity.getBody();
    return sprintList.getSprints();
  }

  protected Sprint getSprint(final int sprintId, final String rapidViewId) {
    ResponseEntity<SprintReport> responseReportEntity = null;
    try {
      responseReportEntity = restTemplate.exchange(rootUrl + SPRINT_REPORT_URL,
          HttpMethod.GET, request, SprintReport.class, rapidViewId, sprintId);
    } catch (RestClientException e) {
      log.error("Cannot get a sprint", e);
      throw new DataSourceConexionException("Cannot get a sprint ", e);
    }
    SprintReport sprintReport = responseReportEntity.getBody();
    return sprintReport.getSprint();
  }


  protected List<Issue> getSprintIssues(final int sprintId) {
    if (ObjectUtils.isEmpty(customFieldDefinitions)) {
      customFieldDefinitions = getCustomFieldDefinitions();
    }

    CustomFieldReplacements customFieldReplacements = new CustomFieldReplacements(
        customFieldDefinitions);

    replacements = customFieldReplacements.getCustomFieldReplacements();
    CustomFieldsNaming customFieldsNaming = new CustomFieldsNaming(replacements);

    List<HttpMessageConverter<?>> converters = new ArrayList<>();
    converters.add(new JacksonConverter(customFieldsNaming));
    restTemplate.setMessageConverters(converters);

    String customFields = StringUtils
        .collectionToCommaDelimitedString(replacements.values());

    // TODO remove message converter at the end.

    return searchIssues("sprint=" + sprintId, DEFAUL_FIELD_LIST, customFields);
  }

  protected List<Issue> getBacklogIssues(final String projectName) {
    CustomFieldReplacements customFieldReplacements = new CustomFieldReplacements(
        customFieldDefinitions);

    replacements = customFieldReplacements.getCustomFieldReplacements();
    CustomFieldsNaming customFieldsNaming = new CustomFieldsNaming(replacements);

    List<HttpMessageConverter<?>> converters = new ArrayList<>();
    converters.add(new JacksonConverter(customFieldsNaming));
    restTemplate.setMessageConverters(converters);

    String customFields = StringUtils
        .collectionToCommaDelimitedString(replacements.values());

    return searchIssues("sprint=null AND project=\"" + projectName + "\"",
        DEFAUL_FIELD_LIST, customFields);
  }

  private List<Issue> searchIssues(String jql, String fields,
      String customfields) {
    List<Issue> issues = new ArrayList<Issue>();
    boolean moreContent = true;
    int startAt = 0;
    while (moreContent) {
      ResponseEntity<IssuesSearchResult> responseEntity = null;
      try {
        responseEntity = restTemplate.exchange(rootUrl + SEARCH_URL,
            HttpMethod.GET, request, IssuesSearchResult.class, jql, fields,
            customfields, startAt, MAX_SEARCH_SIZE);
      } catch (RestClientException e) {
        log.error("Cannot get issues", e);
        throw new DataSourceConexionException("Cannot get issues", e);
      }

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
