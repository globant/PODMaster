package com.globant.agilepodmaster.sync.reading;

import java.util.Date;
import java.util.List;

/**
 * Defines the functionality of a Release builder.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public interface ReleasesBuilder {

  /**
   * Add a release data.
   * 
   * @param name the name of the release.
   * @param projectId the id of the project.
   * @return the same ReleasesBuilder with the release data.
   */
  ReleasesBuilder addRelease(String name, Long projectId);

  /**
   * Add a sprint data.
   * 
   * @param name the name of the sprint.
   * @param startDate the start date.
   * @param endDate the end date.
   * @param tasks tasks of the sprint.
   * @return the same ReleasesBuilder with the sprint data.
   */
  ReleasesBuilder addSprint(String name, Date startDate, Date endDate,
      List<TaskDTO> tasks);

  /**
   * Add a backlog.
   * 
   * @param tasks tasks of the backlog.
   * @return the same ReleasesBuilder with the sprint data.
   */
  ReleasesBuilder addBacklog(List<TaskDTO> tasks);

  /**
   * Add a sprint task data.
   * 
   * @param issueKey the key of the issue.
   * @param summary the summary of the issue.
   * @param type the type of the issue.
   * @param status the status of the issue.
   * @param owner the owner of the issue.
   * @param severity the severity of the issue.
   * @param priority the priority of the issue.
   * @return the same ReleasesBuilder with the task(issue) data.
   */
  /*
  ReleasesBuilder addSprintTasks(String issueKey, String summary, String type,
      String status, String owner, String severity, String priority);
*/
  /**
   * Add a backlog task data.
   * 
   * @param issueKey the key of the issue.
   * @param summary the summary of the issue.
   * @param type the type of the issue.
   * @param status the status of the issue.
   * @param owner the owner of the issue.
   * @param severity the severity of the issue.
   * @param priority the priority of the issue.
   * @return the same ReleasesBuilder with the task(issue) data.
   */
  /*
  ReleasesBuilder addBacklogTask(String issueKey, String summary, String type,
      String status, String owner, String severity, String priority);
*/



}
