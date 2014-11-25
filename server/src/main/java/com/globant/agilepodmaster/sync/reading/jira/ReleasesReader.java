package com.globant.agilepodmaster.sync.reading.jira;

import java.util.List;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintList.SprintItem;
import com.globant.agilepodmaster.sync.reading.jira.responses.SprintReport.Sprint;

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
  
  @Autowired
  IssueTreeBuilder issueTreeBuilder;

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

  private ProductBuilder processProduct(OrganizationBuilder organizationBuilder,
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
      ReleaseReaderConfiguration.Project project, SyncContext context) {
    
    context.info("Processing project: " + project.getJiraName());

    ProjectBuilder projectBuilder = productBuilder.addProject(project
        .getProjectName());

    // TODO we need read Jira versions and iterate per each version.
    ReleaseBuilder releaseBuilder = projectBuilder.withRelease("FAKE Release");
    releaseBuilder = processRelease(releaseBuilder, project, context);
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

      context.info("Processing sprint: " + sprint.getName());

      Sprint jiraSprint = project.getJiraRestClient().getSprint(sprint.getId(),
          project.getJiraRapidViewId());

      if (sprintIsValid(jiraSprint)) {

        backlogBuilder = releaseBuilder.withSprint(sprint.getName(),
            DateUtil.getDate(jiraSprint.getStartDate(), context),
            DateUtil.getDate(jiraSprint.getEndDate(), context));

        backlogBuilder = processSprint(backlogBuilder, project, context, sprint.getId());
        releaseBuilder = backlogBuilder.addToRelease();
      } else {
        context.info("Skipping sprint " + sprint.getName()
            + ", due to invalid or empty dates");
      }
    }

    backlogBuilder = releaseBuilder.withBacklog();
    backlogBuilder = processBacklog(backlogBuilder, project, context);
    releaseBuilder = backlogBuilder.addToRelease();
    
    return releaseBuilder;
  }   
  
  
  private BacklogBuilder processSprint(BacklogBuilder backlogBuilder,
      ReleaseReaderConfiguration.Project project, SyncContext context,
      int sprintId) {
    List<Issue> sprintIssues = project.getJiraRestClient().getSprintIssues(
        sprintId);
    context.info("Collected " + sprintIssues.size() + " sprint issues");

    return processTasks(backlogBuilder, sprintIssues, context);
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

  private TaskBuilder addTask(IssueNode issueNode,
      TaskBuilder taskBuilder) {
    Issue issue = issueNode.getIssue();
    boolean hasTimetracking = issue.getFields().getTimetracking() != null;
    
    String priority = (issue.getFields().getPriority() != null) ? issue
        .getFields().getPriority().getName() : DEFAULT_PRIORITY;

    String type = (issue.getFields().getIssuetype() != null) ? issue
        .getFields().getIssuetype().getName() : DEFAULT_TYPE;

    String status = (issue.getFields().getStatus() != null) ? issue.getFields()
        .getStatus().getName() : DEFAULT_STATUS;

    String severity = (issue.getFields().getSeverity() != null) ? issue
        .getFields().getSeverity().getValue() : DEFAULT_SEVERITY;

    taskBuilder = taskBuilder
        .name(issue.getFields().getSummary())
        .effort(issue.getFields().getStorypoints())
        .type(type)
        .status(status)
        .severity(severity)
        .priority(priority)
        .owner(
            issue.getFields().getAssignee() != null ? issue.getFields()
                .getAssignee().getEmailAddress() : null)

        .estimated(
            hasTimetracking ? issue.getFields().getTimetracking()
                .getOriginalEstimateSeconds() : 0)
        .remaining(
            hasTimetracking ? issue.getFields().getTimetracking()
                .getRemainingEstimateSeconds() : 0)
        .actual(
            hasTimetracking ? (issue.getFields().getTimetracking()
                .getTimeSpentSeconds() != null ? issue.getFields()
                .getTimetracking().getTimeSpentSeconds().intValue() : 0) : 0);

    for (IssueNode subIssueNode : issueNode.getSubIssues()) {
      taskBuilder = taskBuilder.addSubTask();
      taskBuilder = addTask(subIssueNode, taskBuilder);
      taskBuilder = taskBuilder.addToTask();
    }
    return taskBuilder;

  }

  private boolean sprintIsValid(final Sprint sprint) {
    if (sprint == null || StringUtils.isEmpty(sprint.getStartDate())
        || sprint.getStartDate().equalsIgnoreCase("None")
        || StringUtils.isEmpty(sprint.getEndDate())
        || sprint.getEndDate().equalsIgnoreCase("None")) {
      return false;
    }
    return true;
  }

}
