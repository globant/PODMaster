package com.globant.agilepodmaster.sync.reading.jira;

import com.globant.agilepodmaster.sync.datamodel.ProjectDataSet;
import com.globant.agilepodmaster.sync.reading.jira.responses.Issue;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList.SprintItem;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport.Sprint;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author jose.dominguez@globant.com
 *
 */
public class ReleaseReader extends BaseTaskReader {

  /**
   * Constructor.
   * 
   * @param jiraCustomSettings
   */
  public ReleaseReader(JiraCustomSettings jiraCustomSettings) {
    super(jiraCustomSettings);
  }
  
  @Override
  public void readInto(ProjectDataSet.Builder builder) {

    List<SprintItem> sprints = getSprintList();

    if (!CollectionUtils.isEmpty(sprints)) {
      builder.infoMessage("Found " + sprints.size() + " sprints");
    } else {
      builder.warnMessage("No Sprint information found");
    }

    for (SprintItem sprint : sprints) {
      builder.infoMessage("Processing sprint: " + sprint.getName());

      Sprint jiraSprint = getSprint(sprint.getId());

      builder = builder.withRelease(null);

      if (sprintIsValid(jiraSprint)) {

        builder = builder.addSprint(sprint.getName(), jiraSprint);

        List<Issue> sprintIssues = getSprintIssues(sprint.getId());

        for (Issue issue : sprintIssues) {
          builder = builder.addSprintTask(issue.getKey(), issue.getFields());
        }
        builder.infoMessage("Collected " + sprintIssues.size()
            + " sprint issues");

      } else {
        builder.warnMessage("Skipping sprint " + sprint.getName()
            + ", due to invalid or empty dates");
      }
    }

    List<Issue> backlogIssues = getBacklogIssues();
    for (Issue issue : backlogIssues) {
      builder = builder.addBacklogTask(issue.getKey(), issue.getFields());
    }
    builder.infoMessage("Collected " + backlogIssues.size() + "backlog issues");

    builder.build();

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
