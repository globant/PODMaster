package com.globant.agilepodmaster.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.ResourceSupport;

import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Class to collect links of MetricsAggregation.
 * @author Andres Postiglioni.
 *
 */
@EqualsAndHashCode(callSuper = true)
public class MetricsAggregationCollectionResource extends ResourceSupport {
  @Getter
  private final Set<MetricAggregation> aggregated;

  /**
   * Constructor.
   * @param aggregated a list of MetricAgregation.
   */
  @JsonCreator
  public MetricsAggregationCollectionResource(
      @JsonProperty("aggregated") Set<MetricAggregation> aggregated) {
    this.aggregated = aggregated;
  }
}
