package com.globant.agilepodmaster.statistics;

import java.beans.PropertyEditorSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PairPropertyEditor extends PropertyEditorSupport {
  private static final String REGEX = "\\((?<x>\\d+(\\.\\d+)?),(?<y>\\d+(\\.\\d+)?)\\)";
  private static final Pattern PATTERN = Pattern.compile(REGEX);
  
  @Override
  public void setAsText(String text) throws IllegalArgumentException {
    Matcher matcher = PATTERN.matcher(text);

    if (!matcher.find()) {
      throw new IllegalArgumentException("Cannot parse: " + text);
    }

    Double xValue = Double.valueOf(matcher.group("x"));
    Double yValue = Double.valueOf(matcher.group("y"));

    super.setValue(new Pair<Double, Double>(xValue, yValue));
  }

  @Override
  public String getAsText() {
    @SuppressWarnings("unchecked")
    Pair<Double, Double> pair = (Pair<Double, Double>) super.getValue();
    
    return "(" + pair.getX() + "," + pair.getY() + ")";
  }
}
