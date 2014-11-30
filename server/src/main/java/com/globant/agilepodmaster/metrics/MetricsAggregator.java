package com.globant.agilepodmaster.metrics;
import static java.util.stream.Collectors.averagingDouble;

import com.globant.agilepodmaster.core.AbstractMetric;
import com.globant.agilepodmaster.core.ProjectPodMetric;
import com.globant.agilepodmaster.core.SprintPodMetric;
import com.mysema.util.ArrayUtils;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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

    int accStoryPoints = spmStream(list).mapToInt(
        SprintPodMetric::getAccumutaledStoryPoints).sum();
    metrics.add(new Metric<Integer>("accumulated-story-points", accStoryPoints,
        "story points"));

    double accoe = spmStream(list).collect(averagingDouble(SprintPodMetric::getEstimationAccuracy));
    metrics.add(new Metric<Double>("accuracy-of-estimations", accoe, "percentage"));

    StandardDeviation sdEvaluator = new StandardDeviation();  
    double stdVelocity = sdEvaluator.evaluate(buildArrayOfStoryPoints(list));
    
    double avgVelocity = spmStream(list).collect(
        averagingDouble(SprintPodMetric::getAcceptedStoryPoints));
    
    double sov = 1 - avgVelocity / (avgVelocity + stdVelocity);
    metrics.add(new Metric<Double>("stability-of-velocity", sov, "percentage"));
    
    int sumNumberOfBugs = spmStream(list).mapToInt(SprintPodMetric::getNumberOfBugs).sum();
    metrics.add(new Metric<Integer>("bugs", sumNumberOfBugs, "number of bugs"));
    
  }

  private Stream<SprintPodMetric> spmStream(List<AbstractMetric> list) {
    return list.stream().filter(m -> m instanceof SprintPodMetric).map(m -> (SprintPodMetric) m);
  }
  
  private Stream<ProjectPodMetric> pmStream(List<AbstractMetric> list) {
    return list.stream().filter(m -> m instanceof ProjectPodMetric).map(m -> (ProjectPodMetric) m);
  }
  
  private double[] buildArrayOfStoryPoints(List<AbstractMetric> list) {

    List<Integer> result = new ArrayList<Integer>();
    spmStream(list).forEach(
        t -> result.add(Integer.valueOf(t.getAcceptedStoryPoints())));

    double[] resultDouble = new double[result.size()];

    for (int index = 0; index < result.size(); index++) {
      resultDouble[index] = result.get(index).doubleValue();
    }
    return resultDouble;
  }

}
