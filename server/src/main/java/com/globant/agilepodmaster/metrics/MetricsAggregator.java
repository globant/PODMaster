package com.globant.agilepodmaster.metrics;
import static java.util.stream.Collectors.averagingDouble;

import com.globant.agilepodmaster.core.AbstractMetric;
import com.globant.agilepodmaster.core.ProjectMetric;
import com.globant.agilepodmaster.core.ProjectPodMetric;
import com.globant.agilepodmaster.core.SprintPodMetric;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Aggregates metrics depending on collect list.
 * @author Andres Postiglioni.
 *
 */
@Component
public class MetricsAggregator {
  
  /**
   * Aggregates metrics of the list.
   * 
   * @param list list with metrics.
   * @param collect list of metric types to be considered. If collect is null,
   *        all metrics for that type are considered.
   * @return the list of metrics aggregated.
   */
  public Set<Metric<?>> aggregate(List<AbstractMetric> list,
      List<String> collect) {
    Set<Metric<?>> metrics = new HashSet<Metric<?>>();

    this.collectSprintPodMetrics(list, metrics, collect);
    this.collectProjectPodMetrics(list, metrics, collect);
    this.collectProjectMetrics(list, metrics, collect);

    return metrics;
  }
  
  private boolean isMetricShowed(List<String> metricNames, String metricName) {
    return CollectionUtils.isEmpty(metricNames)
        || metricNames.contains(metricName);
  }

  private void collectProjectPodMetrics(List<AbstractMetric> list,
      Set<Metric<?>> metrics, List<String> collect) {
    if (list.stream().filter(m -> m instanceof ProjectPodMetric).count() < 1) {
      return;
    }
    //TODO no metric so far.
  }
  
  private void collectProjectMetrics(List<AbstractMetric> list,
      Set<Metric<?>> metrics, List<String> collect) {
    if (list.stream().filter(m -> m instanceof ProjectMetric).count() < 1) {
      return;
    }

    if (isMetricShowed(collect, "remaining-story-points")) {
      int remainingSp = pmStream(list).findAny().get()
          .getRemainingStoryPoints();
      metrics.add(new Metric<Integer>("remaining-story-points", remainingSp,
          "story points"));
    }

    if (isMetricShowed(collect, "actual-percent-complete")) {
      double actualPercentComplete = pmStream(list).findAny().get()
          .getActualPercentComplete();

      metrics.add(new Metric<Double>("actual-percent-complete",
          actualPercentComplete, "percentage"));
    }
  }

  private void collectSprintPodMetrics(List<AbstractMetric> list,
      Set<Metric<?>> metrics, List<String> collect) {
    if (list.stream().filter(m -> m instanceof SprintPodMetric).count() < 1) {
      return;
    }

    if (isMetricShowed(collect, "velocity")) {
      int sumStoryPoints = spmStream(list).mapToInt(
          SprintPodMetric::getAcceptedStoryPoints).sum();
      metrics.add(new Metric<Integer>("velocity", sumStoryPoints,
          "story points"));
    }

    if (isMetricShowed(collect, "accumulated-story-points")) {
      int accStoryPoints = spmStream(list).mapToInt(
          SprintPodMetric::getAccumutaledStoryPoints).sum();
      metrics.add(new Metric<Integer>("accumulated-story-points",
          accStoryPoints, "story points"));
    }

    if (isMetricShowed(collect, "accuracy-of-estimations")) {
      double accoe = spmStream(list).collect(
          averagingDouble(SprintPodMetric::getEstimationAccuracy));
      metrics.add(new Metric<Double>("accuracy-of-estimations", accoe,
          "percentage"));
    }

    if (isMetricShowed(collect, "stability-of-velocity")) {
      StandardDeviation sdEvaluator = new StandardDeviation();
      double stdVelocity = sdEvaluator.evaluate(buildArrayOfStoryPoints(list));

      double avgVelocity = spmStream(list).collect(
          averagingDouble(SprintPodMetric::getAcceptedStoryPoints));

      double sov = 1 - avgVelocity / (avgVelocity + stdVelocity);
      metrics
          .add(new Metric<Double>("stability-of-velocity", sov, "percentage"));
    }
    
    if (isMetricShowed(collect, "bugs")) {
      int sumNumberOfBugs = spmStream(list).mapToInt(
          SprintPodMetric::getNumberOfBugs).sum();
      metrics
          .add(new Metric<Integer>("bugs", sumNumberOfBugs, "number of bugs"));
    }
    
  }

  private Stream<SprintPodMetric> spmStream(List<AbstractMetric> list) {
    return list.stream().filter(m -> m instanceof SprintPodMetric).map(m -> (SprintPodMetric) m);
  }
  
  private Stream<ProjectMetric> pmStream(List<AbstractMetric> list) {
    return list.stream().filter(m -> m instanceof ProjectMetric).map(m -> (ProjectMetric) m);
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
