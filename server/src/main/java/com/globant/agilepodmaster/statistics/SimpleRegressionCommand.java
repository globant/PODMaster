package com.globant.agilepodmaster.statistics;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.stereotype.Component;

@Component
public class SimpleRegressionCommand {
  public RegressionResult regress(
      Set<Pair<? extends Number, ? extends Number>> data,
      double confidenceLevel, 
      boolean hasIntercept, 
      List<Double> requiredPredictions, 
      Integer precision) {

    double alpha = 1.0 - confidenceLevel;

    SimpleRegression regression = new SimpleRegression(hasIntercept);
    data.forEach(p -> regression.addData(p.getX().doubleValue(), p.getY().doubleValue()));
    double slope = regression.getSlope();

    double delta = regression.getSlopeConfidenceInterval(alpha);

    List<Pair<Double, Double>> predictions = (requiredPredictions != null)
        ? requiredPredictions.stream()
          .map(n -> new Pair<Double, Double>(n, regression.predict(n)))
          .collect(Collectors.toList())
        : new LinkedList<Pair<Double, Double>>();
    
    ConfidenceInterval slopeInterval = 
        new ConfidenceInterval(slope, slope - delta, slope + delta, confidenceLevel);
    
    double intercept = regression.getIntercept();
    
    return new RegressionResult(slopeInterval, intercept, predictions);
  }
}
