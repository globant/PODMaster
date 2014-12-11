package com.globant.agilepodmaster.metrics;

import static java.util.stream.Collectors.averagingDouble;

import com.globant.agilepodmaster.snapshot.AbstractMetric;
import com.globant.agilepodmaster.snapshot.ProjectMetric;
import com.globant.agilepodmaster.snapshot.SprintPodMetric;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Aggregates metrics depending on collect list.
 * 
 * @author Andres Postiglioni.
 *
 */
@Component
@SuppressWarnings("PMD.TooManyMethods")
public class MetricsAggregator {
  
  public static final Map<String, Function<List<AbstractMetric>, Metric<?>>> metricCalculatorMap = 
      createMetricCalculatorMap();
      
  private static final Map<String, Function<List<AbstractMetric>, Metric<?>>> 
        createMetricCalculatorMap() {
    Map<String, Function<List<AbstractMetric>, Metric<?>>> metricCalculatorMap = 
        new HashMap<String, Function<List<AbstractMetric>, Metric<?>>>();
    metricCalculatorMap.put("remaining-story-points",
        MetricsAggregator::calculateRemainigStoryPoints);
    metricCalculatorMap.put("actual-percent-complete",
        MetricsAggregator::calculateActualPercentComplete);
    metricCalculatorMap.put("velocity", MetricsAggregator::calculateVelocity);
    metricCalculatorMap.put("accumulated-story-points",
        MetricsAggregator::calculateAccumulatedStoryPoints);
    metricCalculatorMap.put("accuracy-of-estimations",
        MetricsAggregator::calculateAccuracyOfEstimations);
    metricCalculatorMap.put("stability-of-velocity",
        MetricsAggregator::calculateStabilityOfVelocity);
    metricCalculatorMap.put("bugs", MetricsAggregator::calculateBugs);

    return metricCalculatorMap;
  }
  
  static Metric<?> calculateRemainigStoryPoints(List<AbstractMetric> list) {
    Optional<ProjectMetric> metric = pmStream(list).findAny();
    if (!metric.isPresent()) {
      return null;
    }
    int remainingSp = pmStream(list).findAny().get().getRemainingStoryPoints();
    return new Metric<Integer>("remaining-story-points", remainingSp,
        "story points");
  }

  static Metric<?> calculateActualPercentComplete(
      List<AbstractMetric> list) {
    Optional<ProjectMetric> metric = pmStream(list).findAny();
    if (!metric.isPresent()) {
      return null;
    }
    double actualPC = pmStream(list).findAny().get().getActualPercentComplete();
    return new Metric<Double>("actual-percent-complete", actualPC, "percentage");

  }

  static Metric<?> calculateVelocity(List<AbstractMetric> list) {
    Stream<SprintPodMetric> stream = spmStream(list);
    if (stream.count() == 0) {
      return null;
    }
    int sumStoryPoints = spmStream(list).mapToInt(
        SprintPodMetric::getAcceptedStoryPoints).sum();
    return new Metric<Integer>("velocity", sumStoryPoints, "story points");
  }

  static Metric<?> calculateAccumulatedStoryPoints(
      List<AbstractMetric> list) {
    Stream<SprintPodMetric> stream = spmStream(list);
    if (stream.count() == 0) {
      return null;
    }
    int accStoryPoints = spmStream(list).mapToInt(
        SprintPodMetric::getAccumutaledStoryPoints).sum();
    return new Metric<Integer>("accumulated-story-points", accStoryPoints,
        "story points");
  }

  static Metric<?> calculateAccuracyOfEstimations(
      List<AbstractMetric> list) {
    Stream<SprintPodMetric> stream = spmStream(list);
    if (stream.count() == 0) {
      return null;
    }
    double accoe = spmStream(list).collect(
        averagingDouble(SprintPodMetric::getEstimationAccuracy));
    return new Metric<Double>("accuracy-of-estimations", accoe, "percentage");

  }

  static Metric<?> calculateStabilityOfVelocity(List<AbstractMetric> list) {
    Stream<SprintPodMetric> stream = spmStream(list);
    if (stream.count() == 0) {
      return null;
    }
    List<Integer> arrayOfSP = new ArrayList<Integer>();
    spmStream(list).forEach(
        t -> arrayOfSP.add(Integer.valueOf(t.getAcceptedStoryPoints())));
    double[] arrayOfSPDouble = new double[arrayOfSP.size()];
    for (int index = 0; index < arrayOfSP.size(); index++) {
      arrayOfSPDouble[index] = arrayOfSP.get(index).doubleValue();
    }
    StandardDeviation sdEvaluator = new StandardDeviation();
    double stdVelocity = sdEvaluator.evaluate(arrayOfSPDouble);
    double avgVelocity = spmStream(list).collect(
        averagingDouble(SprintPodMetric::getAcceptedStoryPoints));
    double sov = 1 - avgVelocity / (avgVelocity + stdVelocity);
    return new Metric<Double>("stability-of-velocity", sov, "percentage");
  }

  static Metric<?> calculateBugs(List<AbstractMetric> list) {
    Stream<SprintPodMetric> stream = spmStream(list);
    if (stream.count() == 0) {
      return null;
    }
    int sumNumberOfBugs = spmStream(list).mapToInt(
        SprintPodMetric::getNumberOfBugs).sum();
    return new Metric<Integer>("bugs", sumNumberOfBugs, "number");
  }

  /**
   * Aggregates metrics of the list.
   * 
   * @param list list with metrics.
   * @param calculators list of metric types to be considered. If collect is
   *        null, all metrics for that type are considered.
   * @return the list of metrics aggregated.
   */
  public Set<Metric<?>> aggregate(List<AbstractMetric> list,
      List<Function<List<AbstractMetric>, Metric<?>>> calculators) {

    Set<Metric<?>> metrics = calculators.stream().map(c -> c.apply(list))
        .filter(c -> c != null).collect(Collectors.toSet());

    return metrics;
  }


  private static Stream<SprintPodMetric> spmStream(List<AbstractMetric> list) {
    return list.stream().filter(m -> m instanceof SprintPodMetric).map(m -> (SprintPodMetric) m);
  }
  
  private static Stream<ProjectMetric> pmStream(List<AbstractMetric> list) {
    return list.stream().filter(m -> m instanceof ProjectMetric).map(m -> (ProjectMetric) m);
  }

}
