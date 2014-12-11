package com.globant.agilepodmaster.sync.reading.jira;

import com.globant.agilepodmaster.core.TaskBuilder;
import com.globant.agilepodmaster.sync.BacklogBuilder;
import com.globant.agilepodmaster.sync.OrganizationBuilder;
import com.globant.agilepodmaster.sync.ProductBuilder;
import com.globant.agilepodmaster.sync.ProjectBuilder;
import com.globant.agilepodmaster.sync.ReleaseBuilder;
import com.globant.agilepodmaster.sync.ReleaseReaderConfiguration;
import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.reading.IssueTreeBuilder;
import com.globant.agilepodmaster.sync.reading.IssueTreeBuilder.IssueNode;
import com.globant.agilepodmaster.sync.reading.Reader;
import com.globant.agilepodmaster.sync.reading.ReleasesBuilder;
import com.globant.agilepodmaster.sync.reading.jira.responses.Issue;
import com.globant.agilepodmaster.sync.reading.jira.responses.Issue.Fields;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList.SprintItem;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

import lombok.Setter;

/**
 * Reads releases, sprints, task, backlog.
 *
 * @author jose.dominguez@globant.com
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReleasesReader implements Reader<ReleasesBuilder> {

  private static final String DEFAULT_STATUS = "Open";

  private static final String DEFAULT_TYPE = "Task";

  private static final String DEFAULT_PRIORITY = "Major";

  private static final String DEFAULT_SEVERITY = "Major";

  private static final String SPRINT_CLOSED = "CLOSED";

  private static final String SPRINT_ACTIVE = "ACTIVE";

  @Autowired
  IssueTreeBuilder issueTreeBuilder;

  @Autowired
  SprintReportProcessor sprintReportProcessor;

  @Setter
  private ReleaseReaderConfiguration configuration;

  @Override
  public void readInto(ReleasesBuilder releasesBuilder, SyncContext context) {
    Assert.notNull(configuration, "Configuration cannot be null");
    Assert.notNull(context, "context cannot be null");

    OrganizationBuilder organizationBuilder = releasesBuilder
        .withOrganization(configuration.getOrganizationName());

    for (ReleaseReaderConfiguration.Product product : configuration
        .getProducts()) {
      organizationBuilder = processProduct(organizationBuilder, product,
          context).addToOrganization();
    }
  }

  private ProductBuilder processProduct(
      OrganizationBuilder organizationBuilder,
      ReleaseReaderConfiguration.Product product, SyncContext context) {

    ProductBuilder productBuilder = organizationBuilder.addProduct(product
        .getProductName());

    for (ReleaseReaderConfiguration.Project project : product.getProjects()) {
      productBuilder = processProject(productBuilder, project, context)
          .addToProduct();
    }
    return productBuilder;
  }

  private ProjectBuilder processProject(ProductBuilder productBuilder,
      ReleaseReaderConfiguration.Project projectConfiguration, SyncContext context) {

    context.info("Processing project: " + projectConfiguration.getJiraName());

    ProjectBuilder projectBuilder = productBuilder.addProject(projectConfiguration
        .getProjectName(),projectConfiguration.getPlannedStoryPoints() );

    // TODO we need read Jira versions and iterate per each version.
    ReleaseBuilder releaseBuilder = projectBuilder.withRelease("FAKE Release");
    releaseBuilder = processRelease(releaseBuilder, projectConfiguration, context);
    return releaseBuilder.addToProject();

  }

  private ReleaseBuilder processRelease(ReleaseBuilder releaseBuilder,
      ReleaseReaderConfiguration.Project project, SyncContext context) {

    List<SprintItem> sprints = project.getJiraRestClient().getSprintList(
        project.getJiraRapidViewId());

    if (!CollectionUtils.isEmpty(sprints)) {
      context.info("Found " + sprints.size() + " sprints");
    } else {
      context.warn("No Sprint information found");
    }

    BacklogBuilder backlogBuilder = null;
    for (SprintItem sprint : sprints) {

      SprintReport sprintReport = project.getJiraRestClient().getSprintReport(
          sprint.getId(), project.getJiraRapidViewId());

      if (sprint.getState().equals(SPRINT_CLOSED)) {
        context.info("Processing closed sprint: " + sprint.getName());
        releaseBuilder = sprintReportProcessor.processClosedSprint(
            releaseBuilder, context, sprintReport);
      } else if (sprint.getState().equals(SPRINT_ACTIVE)) {
        context.info("Processing active sprint: " + sprint.getName());
        releaseBuilder = sprintReportProcessor.processActiveSprint(
            releaseBuilder, context, sprintReport);
      } else {
        context.warn("Sprint " + sprint.getName() + " with unknown state: "
            + sprint.getState());
      }
    }

    backlogBuilder = releaseBuilder.withBacklog();
    backlogBuilder = processBacklog(backlogBuilder, project, context);
    releaseBuilder = backlogBuilder.addToRelease();

    return releaseBuilder;
  }

  private BacklogBuilder processBacklog(BacklogBuilder backlogBuilder,
      ReleaseReaderConfiguration.Project project, SyncContext context) {
    List<Issue> backlogIssues = project.getJiraRestClient().getBacklogIssues(
        project.getJiraName());
    context.info("Collected " + backlogIssues.size() + " backlogIssues issues");

    return processTasks(backlogBuilder, backlogIssues, context);

  }

  private BacklogBuilder processTasks(BacklogBuilder backlogBuilder,
      List<Issue> issues, SyncContext context) {

    List<IssueNode> listRoots = issueTreeBuilder.buildTree(issues);

    for (IssueNode issueNode : listRoots) {
      TaskBuilder taskBuilder = addTask(issueNode, backlogBuilder.withTask());
      backlogBuilder = taskBuilder.addToSprint();
    }
    return backlogBuilder;

  }

  private TaskBuilder addTask(IssueNode issueNode, TaskBuilder taskBuilder) {
    Fields issueFields = issueNode.getIssue().getFields();

    taskBuilder = addTaskDetails(taskBuilder, issueFields);

    for (IssueNode subIssueNode : issueNode.getSubIssues()) {
      taskBuilder = taskBuilder.addSubTask();
      taskBuilder = addTask(subIssueNode, taskBuilder);
      taskBuilder = taskBuilder.addToTask();
    }
    return taskBuilder;

  }
  
  @SuppressWarnings("PMD.NPathComplexity")
  private TaskBuilder addTaskDetails(TaskBuilder taskBuilder, Fields issueFields) {
    return taskBuilder
        .name(issueFields.getSummary())
        .effort(issueFields.getStorypoints())
        .type(getIssueType(issueFields))
        .status(getIssueStatus(issueFields))
        .severity(getIssueSeverity(issueFields))
        .priority(getIssuePriority(issueFields))
        .owner(getOwner(issueFields))
        .estimated(
            hasTimetracking(issueFields) ? issueFields.getTimetracking()
                .getOriginalEstimateSeconds() : 0)
        .remaining(
            hasTimetracking(issueFields) ? issueFields.getTimetracking()
                .getRemainingEstimateSeconds() : 0)
        .actual(
            hasTimetracking(issueFields) ? (issueFields.getTimetracking()
                .getTimeSpentSeconds() != null ? issueFields.getTimetracking()
                .getTimeSpentSeconds().intValue() : 0) : 0);
  }

  private String getOwner(Fields issueFields) {
    return issueFields.getAssignee() != null ? issueFields.getAssignee()
        .getEmailAddress() : null;
  }

  private String getIssuePriority(Fields issueFields) {
    return (issueFields.getPriority() != null) ? issueFields.getPriority()
        .getName() : DEFAULT_PRIORITY;
  }

  private String getIssueType(Fields issueFields) {
    return (issueFields.getIssuetype() != null) ? issueFields.getIssuetype()
        .getName() : DEFAULT_TYPE;
  }

  private String getIssueStatus(Fields issueFields) {
    return (issueFields.getStatus() != null) ? issueFields.getStatus()
        .getName() : DEFAULT_STATUS;
  }

  private String getIssueSeverity(Fields issueFields) {
    return (issueFields.getSeverity() != null) ? issueFields.getSeverity()
        .getValue() : DEFAULT_SEVERITY;
  }

  private boolean hasTimetracking(Fields issueFields) {
    return issueFields.getTimetracking() != null;
  }



}
