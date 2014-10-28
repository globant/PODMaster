package com.globant.agilepodmaster.sync.reading.jira;

import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.datamodel.ProjectDataSet;
import com.globant.agilepodmaster.sync.datamodel.ReleaseData;
import com.globant.agilepodmaster.sync.datamodel.SprintData;
import com.globant.agilepodmaster.sync.datamodel.TaskData;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList.SprintItem;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport.Sprint;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author jose.dominguez@globant.com
 *
 */
public class SprintReader extends BaseTaskReader {
  
  private static final String SPRINT_LIST_URL=  
  "rest/greenhopper/1.0/sprintquery/{id}?"
      + "includeFutureSprints=true&includeHistoricSprints=true";
  
  private static final String SPRINT_REPORT_URL= 
  "rest/greenhopper/1.0/rapid/charts/sprintreport?rapidViewId={rapidViewId}"
       + "&sprintId={sprintId}";

  /**
   * Constructor.
   * 
   * @param jiraCustomSettings
   */
  public SprintReader(JiraCustomSettings jiraCustomSettings) {
    super(jiraCustomSettings);
  }
  
  //TODO Define preconditions : POD & MEMBERS should exist.
  @Override
  public void readInto(ProjectDataSet.Builder builder) {

    ResponseEntity<SprintList> responseEntity = restClient.exchange(rootUrl
        + SPRINT_LIST_URL, HttpMethod.GET, request, SprintList.class,
        jiraCustomSettings.getJiraRapidViewId());

    SprintList sprintList = responseEntity.getBody();

    if (!CollectionUtils.isEmpty(sprintList.getSprints())) {
      builder.getSyncContext().info("Found {0} sprints", sprintList.getSprints().size());
    } else {
      builder.getSyncContext().warn("No Sprint information found");
    }

    for (SprintItem sprint : sprintList.getSprints()) {
      builder.getSyncContext().info("Processing sprint '{0}'", sprint.getName());

      ResponseEntity<SprintReport> responseReportEntity = restClient.exchange(
          rootUrl + SPRINT_REPORT_URL, HttpMethod.GET, request,
          SprintReport.class, jiraCustomSettings.getJiraRapidViewId(),
          sprint.getId());

      // #warning Not currently importing Releases (fix versions)
      ReleaseData release = builder.getOrCreateReleaseFor(null);

      SprintReport sprintReport = responseReportEntity.getBody();

      Sprint jiraSprint = sprintReport.getSprint();
      if (sprintIsValid(jiraSprint)) {
        float progress = 0;
        float divisor = sprintReport.getContents().getCompletedIssues().size()
            + sprintReport.getContents().getIncompletedIssues().size()
            + sprintReport.getContents().getPuntedIssues().size() * 100.00f;

        if (divisor != 0) {
          progress = sprintReport.getContents().getCompletedIssues().size()
              / divisor;
        }

        SprintData sprintData = new SprintData(sprint.getName(), release
            .getSprints().size() + 1, getJiraDate(builder.getSyncContext(),
            jiraSprint.getStartDate()), getJiraDate(builder.getSyncContext(),
            jiraSprint.getEndDate()), progress);

        release.getSprints().add(sprintData);
        List<TaskData> tasks = getTaskTree(builder,
            "sprint=" + sprint.getId());
        sprintData.getSprintTasks().addAll(tasks);
        builder.getSyncContext().info("Collected {0} items", tasks.size());

      } else {
        builder.getSyncContext().warn(
            "Skipping sprint '{0}', due to invalid or empty dates",
            sprint.getName());
      }
    }

  }

  private boolean sprintIsValid(final Sprint sprint) {
    if (sprint == null || StringUtils.isEmpty(sprint.getStartDate())
        || sprint.getStartDate() == "None"
        || StringUtils.isEmpty(sprint.getEndDate())
        || sprint.getEndDate() == "None") {
      return false;
    }
    return true;
  }


}
