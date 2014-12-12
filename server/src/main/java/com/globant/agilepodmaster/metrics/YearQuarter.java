package com.globant.agilepodmaster.metrics;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Class the represents a quarter of a year.
 * @author Andres Postiglioni.
 *
 */
@Getter
@EqualsAndHashCode
public class YearQuarter implements Comparable<YearQuarter> {
  private final int year;
  private final Quarter quarter;

  /**
   * Constructor.
   * @param year the year.
   * @param quarter the quarter.
   */
  public YearQuarter(int year, Quarter quarter) {
    this.year = year;
    this.quarter = quarter;
  }
  
  @Override
  public String toString() {
    return year + "/" + quarter;
  }
  
  @Override
  public int compareTo(YearQuarter otherYearQuarter) {
    if (this.year == otherYearQuarter.getYear()) {
      return this.quarter.compareTo(otherYearQuarter.getQuarter());
    }
    return this.year - otherYearQuarter.getYear();
  }
  
}