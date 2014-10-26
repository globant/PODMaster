package com.globant.agilepodmaster.sync.reading.jira;

import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.datamodel.PodData;
import com.globant.agilepodmaster.sync.datamodel.PodData.PodMemberData;
import com.globant.agilepodmaster.sync.datamodel.ProjectDataSet;
import com.globant.agilepodmaster.sync.datamodel.TaskData;
import com.globant.agilepodmaster.sync.reading.jira.responses.Issue;
import com.globant.agilepodmaster.sync.reading.jira.responses.IssuesSearchResult;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Base class for all Jira Tasks readers.
 * TODO try to eliminate this class. It is only used to reuse methods.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public abstract class BaseTaskReader extends BaseJiraReader {

  private static final int MAX_SEARCH_SIZE = 60;
  
  private static final String DEFAUL_FIELD_LIST = "key,parent,priority,summary"
      + ",components,assignee,created,issuetype,status,resolution"
      + ",resolutiondate,timetracking,labels";
  
  private static final String SEARCH_URL = "rest/api/latest/search?jql={jql}"
      + "&fields={fields},{customFields}&startAt={startAt}&maxResults={pageSize}";

  
  private List<String> customFields = new ArrayList<String>();

  /**
   * Constructor.
   * @param syncContext
   * @param jiraCustomSettings
   */
  public BaseTaskReader(SyncContext syncContext,
      JiraCustomSettings jiraCustomSettings) {
    super(syncContext, jiraCustomSettings);
  }

  protected List<TaskData> getTaskTree(ProjectDataSet projectDataSet, String jql) {
    List<Issue> issues = searchIssues(jql, DEFAUL_FIELD_LIST);

    List<TaskData> taskList = new ArrayList<TaskData>();

    for (Issue issue : issues) {
      TaskData task = mapIssueToPodMember(projectDataSet, issue);
      if (task != null) {
        taskList.add(task);
      }
    }
    // TODO Calculate Sub tasks.
    return taskList;
  }

  private List<Issue> searchIssues(String jql, String fields)
  {
      List<Issue> issues = new ArrayList<Issue>();
      boolean moreContent = true;
      int startAt = 0;
      while (moreContent)
      {
        ResponseEntity<IssuesSearchResult> responseEntity = restClient.exchange(
            rootUrl + SEARCH_URL,
            HttpMethod.GET, 
            request, 
            IssuesSearchResult.class,
            jql,
            fields,
            StringUtils.collectionToCommaDelimitedString(this.customFields),
            startAt,
            MAX_SEARCH_SIZE);
        
        IssuesSearchResult issuesSearchResult = responseEntity.getBody();

        if (CollectionUtils.isEmpty(issuesSearchResult.getIssues())) {
              break;
          }

          issues.addAll(issuesSearchResult.getIssues());

          startAt = issuesSearchResult.getStartAt() + MAX_SEARCH_SIZE + 1;
          if (startAt > issuesSearchResult.getTotal())
              moreContent = false;
      }
      return issues;
  }

 

  // TODO do it independent of JIRA format
  protected Date getJiraDate(final SyncContext syncContext, final String sDate) {
    Date date = null;
    try {
      date = new SimpleDateFormat("d/M/y", Locale.ENGLISH).parse(sDate);
    } catch (ParseException e) {
      syncContext.error("Invalid date format:{0}", sDate);
    }
    return date;
  }
  
  private TaskData mapIssueToPodMember(ProjectDataSet projectDataSet,
      Issue issue) {
    PodData pod = null;
    PodMemberData member = null;

    TaskData taskData = new TaskData();

    if (issue.getFields().getAssignee() != null
        && !StringUtils.isEmpty(issue.getFields().getAssignee()
            .getEmailAddress())) {

      String issueEmailAddress = issue.getFields().getAssignee()
          .getEmailAddress();


      member = projectDataSet.getPodMemberByUsername(issueEmailAddress);
      if (member != null) {
        taskData.setPodMember(member.getEmail());
        pod = projectDataSet.getPodByUsername(issueEmailAddress);
      } else {
        if (!jiraCustomSettings.isIgnoreTasksForUnknownMembers()) {
          pod = projectDataSet.getOrCreatePod(PodData.PodTypeData.EXTERNAL);
          syncContext.warn("External user found on '{0}': '{1}'", issue.key,
              issue.fields.assignee.emailAddress);
        } else {
          syncContext.warn("Ignoring item '{0}' for '{1}'", issue.key,
              issue.fields.assignee.emailAddress);
          return null;
        }
      }
    } else {
      pod = projectDataSet.getOrCreatePod(PodData.PodTypeData.UNASSIGNED);
    }


    taskData.setKey(issue.getKey().substring(0,
        Math.min(issue.getKey().length(), 100)));
    taskData.setName(issue.getFields().getSummary()
        .substring(0, Math.min(issue.getFields().getSummary().length(), 100)));
    taskData.setPod(pod.getName());

    // TODO Finish with this mapping

    return taskData;
  }




}
