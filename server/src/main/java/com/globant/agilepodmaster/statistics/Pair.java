package com.globant.agilepodmaster.statistics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Pair<X, Y> {
  final X x;
  final Y y;
  
  @Override
  public String toString() {
    return "(" + x + "," + y + ")";
  }
}
