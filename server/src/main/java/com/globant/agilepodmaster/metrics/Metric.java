package com.globant.agilepodmaster.metrics;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Metric.
 * @author Andres Postiglioni.
 *
 * @param <T>
 */
@ToString
@Getter
@EqualsAndHashCode
public class Metric<T> {
  private final String name;
  private final T value;
  private final String unit;

  /**
   * Constructor.
   * @param name the metric name.
   * @param value the metric value.
   * @param unit the metric unit. 
   */
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
