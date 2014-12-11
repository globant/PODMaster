package com.globant.agilepodmaster.metrics;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.Getter;

/**
 * Quarter enumeration.
 * @author Andres Postiglioni.
 *
 */
public enum Quarter {
  Q1(Calendar.JANUARY, Calendar.MARCH), 
  Q2(Calendar.APRIL, Calendar.JUNE), 
  Q3(Calendar.JULY, Calendar.SEPTEMBER), 
  Q4(Calendar.OCTOBER, Calendar.DECEMBER);
  
  @Getter
  private int startMonth;
  
  @Getter
  private int endMonth;
  
  private Quarter(int startMonth, int endMonth) {
    this.startMonth = startMonth;
    this.endMonth = endMonth;
  }
  
  /**
   * Tells if a date is in this quarter.
   * @param date to be checked.
   * @return true if is in this quarter, false otherwise.
   */
  public boolean isInQuarter(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int dateMonth = calendar.get(Calendar.MONTH);
    return this.startMonth <= dateMonth && dateMonth <= this.endMonth; 
  }
  
  /**
   * Calculates the quarter of a date.
   * @param date the date to be considered.
   * @return the quarter calculated.
   */
  public static Quarter toQuarter(Date date) {
    List<Quarter> quarters = Arrays.asList(Quarter.Q1, Quarter.Q2, Quarter.Q3, Quarter.Q4);
    Quarter quarter = quarters.stream().filter(q -> q.isInQuarter(date)).findAny().get();
    return quarter;
  }
  
  /**
   * Calculates the year quarter of a date.
   * @param date the date to be considered.
   * @return the year quarter calculated.
   */
  public static YearQuarter toYearQuarter(Date date) {
    Quarter quarter = toQuarter(date);
    
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    
    return new YearQuarter(calendar.get(Calendar.YEAR), quarter);
  }
}