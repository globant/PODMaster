package com.globant.agilepodmaster.metrics;
import static java.util.stream.Collectors.averagingDouble;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.SprintPodMetric;

@Component
public class MetricsAggregator {
  public Set<Metric<?>> aggregate(List<SprintPodMetric> list) {
    int sumStoryPoints = list.stream().mapToInt(SprintPodMetric::getAcceptedStoryPoints).sum();
    double accura = list.stream().collect(averagingDouble(SprintPodMetric::getEstimationAccuracy));

    Set<Metric<?>> metrics = new HashSet<Metric<?>>();
    metrics.add(new Metric<Integer>("velocity", sumStoryPoints, "story points"));
    metrics.add(new Metric<Double>("accuracy-of-estimations", accura, "percentage"));

    return metrics;
  }
}
