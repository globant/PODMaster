package com.globant.agilepodmaster.statistics;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RegressionResult {
  private final ConfidenceInterval slopeInterval;
  private final double intercept;
  private final List<Pair<Double, Double>> predictions;
}
