package com.globant.agilepodmaster.statistics;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.util.FastMath;
import org.springframework.stereotype.Component;

@Component
public class NormalApproximationCommand {
  public ConfidenceInterval estimate(Set<? extends Number> data, Double confidenceLevel) {
    double[] values = toDoubleArray(data);

    int numberOfTrials = values.length;
    double std = new StandardDeviation().evaluate(values);

    double mean = StatUtils.mean(values);
    double alpha = (1.0 - confidenceLevel) / 2;

    NormalDistribution normalDistribution = new NormalDistribution();
    double invCumProb = normalDistribution.inverseCumulativeProbability(1 - alpha);
    
    double difference = invCumProb * std / FastMath.sqrt(numberOfTrials);

    ConfidenceInterval interval = 
        new ConfidenceInterval(mean, mean - difference, mean + difference, confidenceLevel);

    return interval;
  }

  private double[] toDoubleArray(Set<? extends Number> data) {
    List<Double> collect = data.stream().map(n -> n.doubleValue()).collect(Collectors.toList());

    double[] values = new double[collect.size()];
    for (int i = 0; i < values.length; i++) {
      values[i] = collect.get(i);
    }
    return values;
  }
}
