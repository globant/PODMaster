package com.globant.agilepodmaster.metrics;
import static java.util.stream.Collectors.averagingDouble;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.AbstractMetric;
import com.globant.agilepodmaster.core.ProjectPodMetric;
import com.globant.agilepodmaster.core.SprintPodMetric;

@Component
public class MetricsAggregator {
  public Set<Metric<?>> aggregate(List<AbstractMetric> list) {
    Set<Metric<?>> metrics = new HashSet<Metric<?>>();

    this.collectSprintPodMetrics(list, metrics);
    this.collectProjectMetrics(list, metrics);

    return metrics;
  }

  private void collectProjectMetrics(List<AbstractMetric> list, Set<Metric<?>> metrics) {
    if (list.stream().filter(m -> m instanceof ProjectPodMetric).count() < 1) {
      return;
    }

    int remainingSp = pmStream(list).mapToInt(ProjectPodMetric::getRemainingStoryPoints).sum();
    metrics.add(new Metric<Integer>("remaining-story-points", remainingSp, "story points"));
  }

  private void collectSprintPodMetrics(List<AbstractMetric> list, Set<Metric<?>> metrics) {
    if (list.stream().filter(m -> m instanceof SprintPodMetric).count() < 1) {
      return;
    }

    int sumStoryPoints = spmStream(list).mapToInt(SprintPodMetric::getAcceptedStoryPoints).sum();
    metrics.add(new Metric<Integer>("velocity", sumStoryPoints, "story points"));

    double accoe = spmStream(list).collect(averagingDouble(SprintPodMetric::getEstimationAccuracy));
    metrics.add(new Metric<Double>("accuracy-of-estimations", accoe, "percentage"));
    
    int sumNumberOfBugs = spmStream(list).mapToInt(SprintPodMetric::getNumberOfBugs).sum();
    metrics.add(new Metric<Integer>("bugs", sumNumberOfBugs, "number of bugs"));
    
  }

  private Stream<SprintPodMetric> spmStream(List<AbstractMetric> list) {
    return list.stream().filter(m -> m instanceof SprintPodMetric).map(m -> (SprintPodMetric) m);
  }
  
  private Stream<ProjectPodMetric> pmStream(List<AbstractMetric> list) {
    return list.stream().filter(m -> m instanceof ProjectPodMetric).map(m -> (ProjectPodMetric) m);
  }
}
