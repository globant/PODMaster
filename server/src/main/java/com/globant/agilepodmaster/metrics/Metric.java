package com.globant.agilepodmaster.metrics;

import lombok.Getter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@ToString
@Getter
public class Metric<T> {
  private final String name;
  private final T value;
  private final String unit;

  @JsonCreator
  public Metric(
      @JsonProperty("name") String name,
      @JsonProperty("value") T value, 
      @JsonProperty("unit") String unit) {
    this.name = name;
    this.value = value;
    this.unit = unit;
  }
}
