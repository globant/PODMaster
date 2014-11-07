package com.globant.agilepodmaster.metrics;

import java.util.Arrays;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.globant.agilepodmaster.core.Pod;
import com.globant.agilepodmaster.core.Sprint;

@Repository
public class SprintPodMetricRepository {
  public Iterable<SprintPodMetric> findAll() {
    Pod pod1 = new Pod("pod1", Pod.Type.Internal);
    Pod pod2 = new Pod("pod2", Pod.Type.Internal);
    Pod pod3 = new Pod("pod3", Pod.Type.Internal);

    Sprint sprint1 = new Sprint("sprint1", null, new Date(), new Date());
    Sprint sprint2 = new Sprint("sprint2", null, new Date(), new Date());

    return Arrays.asList(
        createSprintPodMetric(pod1, sprint1),
        createSprintPodMetric(pod2, sprint1),
        createSprintPodMetric(pod1, sprint2),
        createSprintPodMetric(pod2, sprint2),
        createSprintPodMetric(pod3, sprint1),
        createSprintPodMetric(pod3, sprint2)
    );
  }

  private SprintPodMetric createSprintPodMetric(Pod pod, Sprint sprint) {
    SprintPodMetric spm = new SprintPodMetric(sprint, pod);
    spm.setAcceptedStoryPoints((int) (Math.random() * 100));

    return spm;
  }
}
