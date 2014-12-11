package com.globant.agilepodmaster.readers.releases;

import com.globant.agilepodmaster.readers.releases.jira.responses.SprintReport;
import com.globant.agilepodmaster.readers.releases.jira.responses.SprintReport.Issue;
import com.globant.agilepodmaster.readers.releases.jira.responses.SprintReport.Sprint;
import com.globant.agilepodmaster.release.ReleaseBuilder;
import com.globant.agilepodmaster.sprint.BacklogBuilder;
import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.task.TaskBuilder;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Process a SprintReport and creates a sprint and associated tasks.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Service
public class SprintReportProcessor {

  private static final String DEFAULT_STATUS = "Open";

  private static final String DEFAULT_TYPE = "Task";

  private static final String DEFAULT_PRIORITY = "Major";

  /**
   * Process a SprintReport.
   * 
   * @param releaseBuilder the release builder.
   * @param context the context where to log.
   * @param report the SprintReport object.
   * @return a ReleaseBulder with information of a sprint and its tasks.
   */
  public ReleaseBuilder processClosedSprint(ReleaseBuilder releaseBuilder,
      SyncContext context, SprintReport report) {

    Sprint jiraSprint = report.getSprint();

    if (sprintIsValid(jiraSprint)) {

      BacklogBuilder backlogBuilder = releaseBuilder.withSprint(
          jiraSprint.getName(),
          DateUtil.getDate(jiraSprint.getStartDate(), context),
          DateUtil.getDate(jiraSprint.getEndDate(), context));

      backlogBuilder = processContentOfClosedSprint(backlogBuilder, context,
          report.getContents());

      releaseBuilder = backlogBuilder.addToRelease();
    } else {
      context.info("Skipping sprint " + jiraSprint.getName()
          + ", due to invalid, empty dates or not completed");
    }
    return releaseBuilder;

  }
  
  /**
   * Process a SprintReport of an Active Sprint. For GoPods all these tasks
   * belong to the backlog. It adds to backlog completed and uncompleted tasks
   * of the active sprint.
   * 
   * @param releaseBuilder the release builder.
   * @param context the context where to log.
   * @param report the SprintReport object.
   * @return a ReleaseBuilder with information of a sprint and its tasks.
   */
  public ReleaseBuilder processActiveSprint(ReleaseBuilder releaseBuilder,
      SyncContext context, SprintReport report) {

    BacklogBuilder backlogBuilder = releaseBuilder.withBacklog();
    
    for (Issue issue : report.getContents().getCompletedIssues()) {
      TaskBuilder taskBuilder = addTask(issue, backlogBuilder.withTask());
      backlogBuilder = taskBuilder.addToSprint();
    }

    // Process Incomplete issues
    for (Issue issue : report.getContents().getIncompletedIssues()) {
      TaskBuilder taskBuilder = addTask(issue, backlogBuilder.withTask());
      backlogBuilder = taskBuilder.addToSprint();
    }

    releaseBuilder = backlogBuilder.addToRelease();

    return releaseBuilder;

  }
  
  

  private BacklogBuilder processContentOfClosedSprint(BacklogBuilder backlogBuilder,
      SyncContext context, SprintReport.Contents contents) {

    // Process added issues
    Set<String> addedDuringSprint = contents.getIssueKeysAddedDuringSprint()
        .keySet();

    // Process Complete issues
    for (Issue issue : contents.getCompletedIssues()) {
      TaskBuilder taskBuilder = addTask(issue, backlogBuilder.withTask());
      if (addedDuringSprint.contains(issue.getKey())) {
        taskBuilder = taskBuilder.addedDuringSprint();
      }
      backlogBuilder = taskBuilder.addToSprint();
    }

    // Process Incomplete issues
    for (Issue issue : contents.getIncompletedIssues()) {
      TaskBuilder taskBuilder = addTask(issue, backlogBuilder.withTask());
      taskBuilder = taskBuilder.removedDuringSprint();
      backlogBuilder = taskBuilder.addToSprint();
    }

    // Process punted issues
    for (Issue issue : contents.getPuntedIssues()) {
      TaskBuilder taskBuilder = addTask(issue, backlogBuilder.withTask());
      taskBuilder = taskBuilder.removedDuringSprint();
      backlogBuilder = taskBuilder.addToSprint();
    }

    return backlogBuilder;

  }

  private TaskBuilder addTask(Issue issue, TaskBuilder taskBuilder) {

    String priority = assigNotEmptyOrDefault(issue.getPriorityName(),
        DEFAULT_PRIORITY);
    String type = assigNotEmptyOrDefault(issue.getTypeName(), DEFAULT_TYPE);
    String status = assigNotEmptyOrDefault(issue.getStatusName(),
        DEFAULT_STATUS);

    float effort = issue.getEstimateStatistic() != null ? issue
        .getEstimateStatistic().getStatFieldValue().getValue() : 0;

    taskBuilder = taskBuilder.name(issue.getSummary()).effort(effort)
        .type(type).status(status).priority(priority)
        .owner(issue.getAssignee());

    return taskBuilder;

  }

  private String assigNotEmptyOrDefault(String value, String defaultValue) {
    return StringUtils.isEmpty(value) ? defaultValue : value;
  }


  private boolean sprintIsValid(final Sprint sprint) {
    if (sprint == null || StringUtils.isEmpty(sprint.getStartDate())
        || sprint.getStartDate().equalsIgnoreCase("None")
        || StringUtils.isEmpty(sprint.getEndDate())
        || sprint.getEndDate().equalsIgnoreCase("None")
        || StringUtils.isEmpty(sprint.getCompleteDate())
        || sprint.getCompleteDate().equalsIgnoreCase("None")) {
      return false;
    }
    return true;
  }
}
