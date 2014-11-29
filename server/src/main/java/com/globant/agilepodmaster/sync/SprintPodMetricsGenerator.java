package com.globant.agilepodmaster.sync;

import com.globant.agilepodmaster.core.Pod;
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
public class SprintPodMetricsGenerator {

  /**
   * Generates a List of SprintPodMetrics. 
   * @param pods the pods considered for these SprintPodMetrics.
   * @param sprints  the sprints considered for these SprintPodMetrics.
   * @param tasks all the tasks of these sprints.
   * @return the list of SprintPodMetrics.
   */
  public List<SprintPodMetric> generates(Set<Pod> pods, Set<Sprint> sprints,
      Set<Task> tasks) {
    List<SprintPodMetric> sprintPodMetrics = new ArrayList<SprintPodMetric>();

    for (Pod pod : pods) {
      for (Sprint sprint : sprints) {

        double removedStoryPoints = streamFor(pod, sprint, tasks)
            .filter(
                t -> t.getChangeDuringSprint() == ChangeDuringSprint.REMOVED)
            .mapToDouble(Task::getEffort).sum();

        double addedStoryPoints = streamFor(pod, sprint, tasks)
            .filter(
                t -> t.getChangeDuringSprint() == ChangeDuringSprint.ADDED
                    && t.isAccepted()).mapToDouble(Task::getEffort).sum();

        double noChangedStoryPoints = streamFor(pod, sprint, tasks)
            .filter(
                t -> t.getChangeDuringSprint() == ChangeDuringSprint.NOCHANGE)
            .mapToDouble(Task::getEffort).sum();

        double velocity = noChangedStoryPoints + addedStoryPoints;
        streamFor(pod, sprint, tasks).filter(t -> t.isAccepted())
            .mapToDouble(Task::getEffort).sum();

        double plannedEffort = noChangedStoryPoints + removedStoryPoints;

        long numberOfBugs = streamFor(pod, sprint, tasks)
            .filter(t -> t.isBug()).count();

        SprintPodMetric spm = new SprintPodMetric(sprint, pod);
        spm.setAcceptedStoryPoints((int) velocity);
        spm.setPlannedStoryPoints((int) plannedEffort);
        spm.setNumberOfBugs((int) numberOfBugs);

        sprintPodMetrics.add(spm);
      }
    }
    return sprintPodMetrics;

  }

  private Stream<Task> streamFor(Pod pod, Sprint sprint, Set<Task> tasks) {
    return tasks
        .stream()
        .filter(
            t -> sprint.equals(t.getSprint()) && t.getOwner() != null
                && pod.equals(t.getOwner().getPod()))
        .collect(Collectors.toList()).stream();
  }


}
