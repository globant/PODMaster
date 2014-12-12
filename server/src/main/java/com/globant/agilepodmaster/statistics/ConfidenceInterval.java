package com.globant.agilepodmaster.statistics;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ConfidenceInterval {
  /** The center of the interval. **/
  private final double center;

  /** Lower endpoint of the interval. */
  private final double lowerBound;

  /** Upper endpoint of the interval. */
  private final double upperBound;

  /**
   * The asserted probability that the interval contains the population
   * parameter.
   */
  private final double confidenceLevel;
}
