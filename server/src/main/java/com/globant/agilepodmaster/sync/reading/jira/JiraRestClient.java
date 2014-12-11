package com.globant.agilepodmaster.sync.reading.jira;



import com.globant.agilepodmaster.sync.ConexionResponseErrorException;
import com.globant.agilepodmaster.sync.ConexionRestException;
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
import org.springframework.web.client.HttpStatusCodeException;
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
    log.info("Getting custom fields ...");
    return connect(rootUrl + FIELD_LIST_URL, CustomFieldDefinition[].class);
  }

  protected List<SprintItem> getSprintList(final String rapidViewId) {
    
    log.info("Getting Sprint list ...");
    SprintList sprintList  = connect(rootUrl + SPRINT_LIST_URL, SprintList.class, rapidViewId);
    return sprintList.getSprints();
  }

  protected SprintReport getSprintReport(final int sprintId, final String rapidViewId) {
    
    log.info("Getting Sprint details ...");
    SprintReport sprintReport = connect(rootUrl + SPRINT_REPORT_URL, SprintReport.class
        , rapidViewId, sprintId);
    
    return sprintReport;
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

    return searchIssues("sprint=null AND project=\"" + projectName + "\"",
        DEFAUL_FIELD_LIST, customFields);
  }
  
  
  private <T> T connect(String url, Class<T> responseType,
      Object... uriVariables) {

    ResponseEntity<T> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(url, HttpMethod.GET, request,
          responseType, uriVariables);
    } catch (HttpStatusCodeException e) {
      log.error("Cannot Connect - Response Error: ",
          e.getResponseBodyAsString());
      throw new ConexionResponseErrorException(
          "Cannot Connect - Response Error: " + e.getResponseBodyAsString());
    } catch (RestClientException e) {
      log.error("Cannot connect to data source ", e);
      throw new ConexionRestException("Cannot connect to data source ", e);
    }
    return responseEntity.getBody();
  }

  private List<Issue> searchIssues(String jql, String fields,
      String customfields) {
    List<Issue> issues = new ArrayList<Issue>();
    boolean moreContent = true;
    int startAt = 0;
    while (moreContent) {
      
      log.info("Getting issues ...");
      IssuesSearchResult issuesSearchResult = connect(rootUrl + SEARCH_URL
          , IssuesSearchResult.class, jql, fields,
          customfields, startAt, MAX_SEARCH_SIZE);
    
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
