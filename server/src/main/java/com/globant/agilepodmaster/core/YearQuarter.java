package com.globant.agilepodmaster.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class YearQuarter {
  private final int year;
  private final Quarter quarter;

  public YearQuarter(int year, Quarter quarter) {
    this.year = year;
    this.quarter = quarter;
  }
  
  @Override
  public String toString() {
    return year + "/" + quarter;
  }
}