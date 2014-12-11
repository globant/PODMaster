package com.globant.agilepodmaster.sync;

import com.globant.agilepodmaster.core.Pod;
import com.globant.agilepodmaster.core.Project;
import com.globant.agilepodmaster.core.ProjectMetric;
import com.globant.agilepodmaster.core.Release;
import com.globant.agilepodmaster.core.Sprint;
import com.globant.agilepodmaster.core.SprintPodMetric;
import com.globant.agilepodmaster.core.Task;
import com.globant.agilepodmaster.core.Task.ChangeDuringSprint;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class to build a list of SprintPodMetrics.
 * @author jose.dominguez@globant.com
 *
 */
@Service
public class MetricsGenerator {
    
  /**
   * Generates a List of SprintPodMetrics.
   * 
   * @param projects the projects considered for these ProjectMetrics.
   * @param tasks all the tasks of these sprints.
   * @return the list of SprintPodMetrics.
   */
  public List<ProjectMetric> generatesProjectMetrics(Set<Project> projects,
      Set<Task> tasks) {
    List<ProjectMetric> projectMetrics = new ArrayList<ProjectMetric>();

    for (Project project : projects) {

      double removedStoryPoints = getRemovedStoryPoints(streamFor(project,
          tasks));
      double addedStoryPoints = getAddedStoryPoints(streamFor(project, tasks));
      double noChangedStoryPoints = getNoChangedStoryPoints(streamFor(project,
          tasks));

      double velocity = noChangedStoryPoints + addedStoryPoints;
      int plannedStoryPoints = project.getPlannedStoryPoints();

      ProjectMetric pm = new ProjectMetric(project);
      pm.setRemainingStoryPoints(plannedStoryPoints - (int) velocity);
      if (velocity != 0) {
        pm.setActualPercentComplete(velocity / plannedStoryPoints);
      }
      projectMetrics.add(pm);

    }
    return projectMetrics;

  }
  
  /**
   * Generates a List of SprintPodMetrics.
   * 
   * @param pods the pods considered for these SprintPodMetrics.
   * @param sprints the sprints considered for these SprintPodMetrics.
   * @param tasks all the tasks of these sprints.
   * @return the list of SprintPodMetrics.
   */
  public List<SprintPodMetric> generatesSprintPodMetrics(Set<Pod> pods,
      Set<Sprint> sprints, Set<Task> tasks) {
    List<SprintPodMetric> sprintPodMetrics = new ArrayList<SprintPodMetric>();


    List<Sprint> sortedSprints = asSortedList(sprints);

    double accumulatedStoryPoints = 0;

    Release lastRelease = null;

    for (Pod pod : pods) {
      accumulatedStoryPoints = 0;
      for (Sprint sprint : sortedSprints) {

        if (lastRelease == null || sprint.getRelease() != lastRelease) {
          accumulatedStoryPoints = 0;
        }

        double removedStoryPoints = getRemovedStoryPoints(streamFor(pod,
            sprint, tasks));
        double addedStoryPoints = getAddedStoryPoints(streamFor(pod, sprint,
            tasks));
        double noChangedStoryPoints = getNoChangedStoryPoints(streamFor(pod,
            sprint, tasks));

        double velocity = noChangedStoryPoints + addedStoryPoints;
        accumulatedStoryPoints = accumulatedStoryPoints + velocity;
        double plannedEffort = noChangedStoryPoints + removedStoryPoints;
        long numberOfBugs = streamFor(pod, sprint, tasks)
            .filter(t -> t.isBug()).count();

        SprintPodMetric spm = new SprintPodMetric(sprint, pod);
        spm.setAcceptedStoryPoints((int) velocity);
        spm.setPlannedStoryPoints((int) plannedEffort);
        spm.setNumberOfBugs((int) numberOfBugs);
        spm.setAccumutaledStoryPoints((int) accumulatedStoryPoints);

        sprintPodMetrics.add(spm);

        lastRelease = sprint.getRelease();
      }
    }
    return sprintPodMetrics;

  }
  
  private double getRemovedStoryPoints(Stream<Task> stream) {
    return stream
        .filter(t -> t.getChangeDuringSprint() == ChangeDuringSprint.REMOVED)
        .mapToDouble(Task::getEffort).sum();
  }

  private double getAddedStoryPoints(Stream<Task> stream) {
    return stream
      .filter(
          t -> t.getChangeDuringSprint() == ChangeDuringSprint.ADDED
              && t.isAccepted()).mapToDouble(Task::getEffort).sum();
  }  

  private double getNoChangedStoryPoints(Stream<Task> stream) {
    return stream
      .filter(
          t -> t.getChangeDuringSprint() == ChangeDuringSprint.NOCHANGE)
      .mapToDouble(Task::getEffort).sum();
  }   

  private Stream<Task> streamFor(Pod pod, Sprint sprint, Set<Task> tasks) {
    return tasks
        .stream()
        .filter(
            t -> sprint.equals(t.getSprint()) && t.getOwner() != null
                && pod.equals(t.getOwner().getPod()))
        .collect(Collectors.toList()).stream();
  }
  
  private Stream<Task> streamFor(Project project, Set<Task> tasks) {
    return tasks
        .stream()
        .filter(
            t -> project.equals(t.getRelease().getProject()) && t.getSprint() != null)
        .collect(Collectors.toList()).stream();
  }

  private List<Sprint> asSortedList(Set<Sprint> sprintSet) {
    List<Sprint> list = new ArrayList<Sprint>(sprintSet);
    java.util.Collections.sort(list);
    return list;
  }

}
