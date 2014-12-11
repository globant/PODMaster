package com.globant.agilepodmaster.statistics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Class that represents a pair.
 * @author Andres Postigioni.
 *
 * @param <X>
 * @param <Y>
 */
@Getter
@RequiredArgsConstructor
public class Pair<X, Y> {
  final X valueX;
  final Y valueY;
  
  @Override
  public String toString() {
    return "(" + valueX + "," + valueY + ")";
  }
}
