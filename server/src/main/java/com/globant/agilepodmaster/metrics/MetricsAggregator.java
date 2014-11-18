package com.globant.agilepodmaster.metrics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.SprintPodMetric;

@Component
public class MetricsAggregator {
  public Set<Metric<?>> aggregate(List<SprintPodMetric> list) {
    Set<Metric<?>> metrics = new HashSet<Metric<?>>();

    int sumStoryPoints = list.stream().mapToInt(SprintPodMetric::getAcceptedStoryPoints).sum();
    metrics.add(new Metric<Integer>("velocity", sumStoryPoints, "story points"));

    return metrics;
  }
}
