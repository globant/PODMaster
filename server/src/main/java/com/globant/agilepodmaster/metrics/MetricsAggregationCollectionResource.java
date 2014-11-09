package com.globant.agilepodmaster.metrics;

import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@EqualsAndHashCode(callSuper = true)
public class MetricsAggregationCollectionResource extends ResourceSupport {
  @Getter
  private final Set<MetricAggregation> aggregated;

  @JsonCreator
  public MetricsAggregationCollectionResource(
      @JsonProperty("aggregated") Set<MetricAggregation> aggregated) {
    this.aggregated = aggregated;
  }
}
