package com.globant.agilepodmaster.metrics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class MetricsAggregator {
  public Set<Metric<?>> aggregate(List<SprintPodMetric> list) {
    Set<Metric<?>> metrics = new HashSet<Metric<?>>();

    int sumHoursAccepted = list.stream().mapToInt(SprintPodMetric::getHoursInAcceptedStories).sum();
    metrics.add(new Metric<Integer>("velocity", sumHoursAccepted, "hours"));

    int sumStoryPoints = list.stream().mapToInt(SprintPodMetric::getAcceptedStoryPoints).sum();
    metrics.add(new Metric<Integer>("velocity", sumStoryPoints, "story points"));

    return metrics;
  }
}
