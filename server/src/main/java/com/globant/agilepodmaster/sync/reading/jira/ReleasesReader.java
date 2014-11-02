package com.globant.agilepodmaster.sync.reading.jira;

import com.globant.agilepodmaster.sync.reading.Reader;
import com.globant.agilepodmaster.sync.reading.ReleasesBuilder;
import com.globant.agilepodmaster.sync.reading.jira.responses.Issue;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList.SprintItem;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport.Sprint;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import lombok.Setter;

/**
 * Reads releases, sprints, task, backlog.
 * @author jose.dominguez@globant.com
 *
 */
@Service
public class ReleasesReader implements Reader<ReleasesBuilder> {

  @Setter
  private JiraRestClient restJiraClient;

  @Setter
  private JiraCustomSettings settings;

  @Override
  public void readInto(ReleasesBuilder builder) {

    Assert.notNull(restJiraClient, "RestJiraClient must not be null");
    Assert.notNull(settings, "JiraCustomSettings must not be null");

    List<SprintItem> sprints = restJiraClient.getSprintList(settings
        .getJiraRapidViewId());

    if (!CollectionUtils.isEmpty(sprints)) {
      builder.infoMessage("Found " + sprints.size() + " sprints");
    } else {
      builder.warnMessage("No Sprint information found");
    }

    for (SprintItem sprint : sprints) {
      builder.infoMessage("Processing sprint: " + sprint.getName());

      Sprint jiraSprint = restJiraClient.getSprint(sprint.getId(),
          settings.getJiraRapidViewId());

      builder = builder.addRelease(null);

      if (sprintIsValid(jiraSprint)) {

        builder = builder.addSprint(sprint.getName(), jiraSprint.startDate,
            jiraSprint.endDate);

        List<Issue> sprintIssues = restJiraClient.getSprintIssues(sprint
            .getId());

        // TODO Add more fields. Builder should not know anything about JIRA.
        for (Issue issue : sprintIssues) {
          builder = builder.addSprintTask(issue.getKey(), issue.getFields()
              .getSummary(), null, null, null, null, null);
        }
        builder.infoMessage("Collected " + sprintIssues.size()
            + " sprint issues");

      } else {
        builder.warnMessage("Skipping sprint " + sprint.getName()
            + ", due to invalid or empty dates");
      }
    }

    List<Issue> backlogIssues = restJiraClient.getBacklogIssues(settings
        .getJiraProjectName());
    for (Issue issue : backlogIssues) {
      builder = builder.addBacklogTask(issue.getKey(), issue.getFields()
          .getSummary(), null, null, null, null, null);
    }
    builder.infoMessage("Collected " + backlogIssues.size() + "backlog issues");


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
