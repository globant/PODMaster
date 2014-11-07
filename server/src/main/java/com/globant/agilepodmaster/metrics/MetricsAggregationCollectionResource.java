package com.globant.agilepodmaster.metrics;

import java.util.List;

import lombok.Getter;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MetricsAggregationCollectionResource extends ResourceSupport {
  @Getter
  private final List<MetricAggregation> aggregated;

  @JsonCreator
  public MetricsAggregationCollectionResource(
      @JsonProperty("aggregated") List<MetricAggregation> aggregated) {
    this.aggregated = aggregated;
  }
}
