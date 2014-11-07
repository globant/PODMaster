package com.globant.agilepodmaster.core;

import lombok.Getter;

@Getter
public class YearQuarter {
  private final int year;
  private final Quarter quarter;

  public YearQuarter(int year, Quarter quarter) {
    this.year = year;
    this.quarter = quarter;
  }
}