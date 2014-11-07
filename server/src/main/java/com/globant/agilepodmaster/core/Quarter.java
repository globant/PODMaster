package com.globant.agilepodmaster.core;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.Getter;

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
  
  public boolean isInQuarter(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int dateMonth = calendar.get(Calendar.MONTH);
    return this.startMonth <= dateMonth && dateMonth <= this.endMonth; 
  }
  
  public static Quarter toQuarter(Date date) {
    List<Quarter> quarters = Arrays.asList(Quarter.Q1, Quarter.Q2, Quarter.Q3, Quarter.Q4);
    Quarter quarter = quarters.stream().filter((q) -> q.isInQuarter(date)).findAny().get();
    return quarter;
  }
  
  public static YearQuarter toYearQuarter(Date date) {
    Quarter quarter = toQuarter(date);
    
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    
    return new YearQuarter(calendar.get(Calendar.YEAR), quarter);
  }
}