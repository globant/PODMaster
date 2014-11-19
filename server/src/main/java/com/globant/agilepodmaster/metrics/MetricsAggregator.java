package com.globant.agilepodmaster.metrics;
import static java.util.stream.Collectors.averagingDouble;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.MetricData;
import com.globant.agilepodmaster.core.ProjectMetric;
import com.globant.agilepodmaster.core.SprintPodMetric;

@Component
public class MetricsAggregator {
  public Set<Metric<?>> aggregate(List<MetricData> list) {
    Set<Metric<?>> metrics = new HashSet<Metric<?>>();

    this.collectSprintPodMetrics(list, metrics);
    this.collectProjectMetrics(list, metrics);

    return metrics;
  }

  private void collectProjectMetrics(List<MetricData> list, Set<Metric<?>> metrics) {
    if (list.stream().filter(m -> m instanceof ProjectMetric).count() < 1) {
      return;
    }

    int remainingSp = pmStream(list).mapToInt(ProjectMetric::getRemainingStoryPoints).sum();
    metrics.add(new Metric<Integer>("remaining-story-points", remainingSp, "story points"));
  }

  private void collectSprintPodMetrics(List<MetricData> list, Set<Metric<?>> metrics) {
    if (list.stream().filter(m -> m instanceof SprintPodMetric).count() < 1) {
      return;
    }

    int sumStoryPoints = spmStream(list).mapToInt(SprintPodMetric::getAcceptedStoryPoints).sum();
    metrics.add(new Metric<Integer>("velocity", sumStoryPoints, "story points"));

    double accoe = spmStream(list).collect(averagingDouble(SprintPodMetric::getEstimationAccuracy));
    metrics.add(new Metric<Double>("accuracy-of-estimations", accoe, "percentage"));
  }

  private Stream<SprintPodMetric> spmStream(List<MetricData> list) {
    return list.stream().filter(m -> m instanceof SprintPodMetric).map(m -> (SprintPodMetric) m);
  }
  
  private Stream<ProjectMetric> pmStream(List<MetricData> list) {
    return list.stream().filter(m -> m instanceof ProjectMetric).map(m -> (ProjectMetric) m);
  }
}
