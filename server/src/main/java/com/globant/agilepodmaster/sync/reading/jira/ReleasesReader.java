package com.globant.agilepodmaster.sync.reading.jira;

import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.reading.Reader;
import com.globant.agilepodmaster.sync.reading.ReleasesBuilder;
import com.globant.agilepodmaster.sync.reading.TaskDTO;
import com.globant.agilepodmaster.sync.reading.jira.responses.Issue;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList.SprintItem;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport.Sprint;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Setter;

/**
 * Reads releases, sprints, task, backlog.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReleasesReader implements Reader<ReleasesBuilder> {

  @Setter
  private JiraRestClient restJiraClient;

  @Setter
  // this per project
  private List<JiraCustomSettings> settingsList;


  @Override
  public void readInto(ReleasesBuilder releasesBuilder, SyncContext context) {
    Assert.notNull(restJiraClient, "RestJiraClient cannot be null");
    Assert.notNull(context, "context cannot be null");

    for (JiraCustomSettings settings : settingsList) {
      readProjectReleases(settings, releasesBuilder, context);
    }

  }

  private void readProjectReleases(JiraCustomSettings settings,
      ReleasesBuilder releaseBuilder, SyncContext context) {
    Assert.notNull(restJiraClient, "RestJiraClient cannot be null");
    Assert.notNull(settings, "JiraCustomSettings cannot be null");
    Assert.notNull(releaseBuilder, "builder cannot be null");


    // TODO we need read Jira versions and iterate per each version.
    releaseBuilder = releaseBuilder.addRelease("FAKE Release",
        settings.getProjectId());

    List<SprintItem> sprints = restJiraClient.getSprintList(settings
        .getJiraRapidViewId());

    if (!CollectionUtils.isEmpty(sprints)) {
      context.info("Found " + sprints.size() + " sprints");
    } else {
      context.warn("No Sprint information found");
    }

    for (SprintItem sprint : sprints) {
      context.info("Processing sprint: " + sprint.getName());

      Sprint jiraSprint = restJiraClient.getSprint(sprint.getId(),
          settings.getJiraRapidViewId());

      if (sprintIsValid(jiraSprint)) {

        List<Issue> sprintIssues = restJiraClient.getSprintIssues(sprint
            .getId());

        releaseBuilder = releaseBuilder.addSprint(sprint.getName(),
            DateUtil.getDate(jiraSprint.getStartDate(), context),
            DateUtil.getDate(jiraSprint.getEndDate(), context),
            buildTaskTree(sprintIssues, context));

      } else {
        context.info("Skipping sprint " + sprint.getName()
            + ", due to invalid or empty dates");
      }

    }

    List<Issue> backlogIssues = restJiraClient.getBacklogIssues(settings
        .getJiraProjectName());

    context.info("Processing backlog");
    
    releaseBuilder.addBacklog(buildTaskTree(backlogIssues,
        context));

    return;

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

  /**
   * @return a list of roots.
   */
  private List<TaskDTO> buildTaskTree(List<Issue> issues, SyncContext context) {

    Map<Issue, TaskDTO> mappedPairs = new HashMap<Issue, TaskDTO>();

    context.info("Collected " + issues.size() + " sprint issues");
    
    for (Issue issue : issues) {
      TaskDTO taskDTO = buildTaskDTO(issue, context);
      mappedPairs.put(issue, taskDTO);
    }
    
    addSubTasksToMap(mappedPairs, context);
    return  getRootsDTOs(mappedPairs);

  }
  
  private List<TaskDTO> getRootsDTOs(Map<Issue, TaskDTO> mappedPairs) {

    List<TaskDTO> taskRootsDTOs = new ArrayList<TaskDTO>();
    for (Map.Entry<Issue, TaskDTO> pair : mappedPairs.entrySet()) {
      if (pair.getKey().getFields().getParent() == null) {
        taskRootsDTOs.add(pair.getValue());
      }
    }
    return taskRootsDTOs;
  }
  
  private void addSubTasksToMap(Map<Issue, TaskDTO> mappedPairs, SyncContext context) {

    for (Map.Entry<Issue, TaskDTO> pair : mappedPairs.entrySet()) {
      if (pair.getKey().getFields().getParent() != null) {

        Issue parentIssue = searchIssueById(mappedPairs.keySet(), pair.getKey()
            .getId());

        if (parentIssue != null) {
          mappedPairs.get(parentIssue).getSubTasks().add(pair.getValue());
        } else {
          context.warn("Parent item " + pair.getKey().getId()
              + " can't be found in result");
        }
      }
    }
  }

  private TaskDTO buildTaskDTO(Issue issue, SyncContext context) {

    TaskDTO.Builder builder = new TaskDTO.Builder(context)
        .name(issue.getFields().getSummary())
        .effort(issue.getFields().getStorypoints())

        .createDate(issue.getFields().getCreated())
        .type(
            (issue.getFields().getIssuetype() != null) ? issue.getFields()
                .getIssuetype().getName() : null)
        .status(
            (issue.getFields().getStatus() != null) ? issue.getFields()
                .getStatus().getName() : null)
        .severity(
            (issue.getFields().getSeverity() != null) ? issue.getFields()
                .getSeverity().getValue() : null)
        .priority(
            (issue.getFields().getPriority() != null) ? issue.getFields()
                .getPriority().getName() : null);
    
    if (issue.getFields().getAssignee() != null) {
      builder.owner(issue.getFields().getAssignee().getEmailAddress());
    }

    if (issue.getFields().getTimetracking() != null) {
      builder
          .estimated(
              issue.getFields().getTimetracking().getOriginalEstimateSeconds())
          .remaining(
              issue.getFields().getTimetracking().getRemainingEstimateSeconds())
          .actual(issue.getFields().getTimetracking().getTimeSpentSeconds());
    }

    return builder.build();

  }

  private Issue searchIssueById(Set<Issue> issues, String id) {
    Issue result = null;
    for (Issue issue : issues) {
      if (issue.getId().equals(id)) {
        result = issue;
      }
    }
    return null;
  }



}
